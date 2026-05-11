package it.unical.progettoweb.service;

import it.unical.progettoweb.dao.impl.RealEstateDaoImpl;
import it.unical.progettoweb.model.RealEstate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RealEstateDaoImpl realEstateDao;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${groq.api.key}")
    private String groqApiKey;

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL = "llama3-8b-8192";

    public String rispondi(String domanda) {
        String contesto = estraiContestoDalDb(domanda);
        List<Map<String, String>> messaggi = new ArrayList<>();

        messaggi.add(Map.of(
                "role", "system",
                "content", """
                Sei un assistente immobiliare esperto che aiuta gli utenti del sito ProgettoWeb,
                una piattaforma di annunci immobiliari.
                
                Rispondi SOLO a domande inerenti a:
                - Zone e quartieri delle città
                - Prezzi degli immobili
                - Tipologie di immobili (appartamenti, ville, garage, terreni)
                - Consigli sull'acquisto/affitto immobiliare
                - Informazioni sugli annunci presenti nella piattaforma
                
                Se la domanda non riguarda questi argomenti, rispondi educatamente che
                puoi aiutare solo con domande immobiliari.
                
                Rispondi sempre in italiano, in modo chiaro e conciso.
                Basa le tue risposte SUI DATI REALI forniti nel contesto quando disponibili.
                NON inventare prezzi o dati non presenti nel contesto.
                """
        ));

        if (!contesto.isEmpty()) {
            messaggi.add(Map.of(
                    "role", "system",
                    "content", "DATI REALI DAL DATABASE DELLA PIATTAFORMA:\n" + contesto
            ));
        }

        messaggi.add(Map.of("role", "user", "content", domanda));

        return chiamaGroq(messaggi);
    }

    private String estraiContestoDalDb(String domanda) {
        try {
            List<RealEstate> tutti = realEstateDao.findAll();
            if (tutti == null || tutti.isEmpty()) return "";

            StringBuilder sb = new StringBuilder();
            String domandaLower = domanda.toLowerCase();

            Map<String, List<RealEstate>> perCitta = tutti.stream()
                    .filter(r -> r.getAddress() != null)
                    .collect(Collectors.groupingBy(r -> estraiCitta(r.getAddress())));

            String cittaMenzionata = perCitta.keySet().stream()
                    .filter(c -> domandaLower.contains(c.toLowerCase()))
                    .findFirst().orElse(null);

            Map<String, List<RealEstate>> datiRilevanti = cittaMenzionata != null
                    ? Map.of(cittaMenzionata, perCitta.getOrDefault(cittaMenzionata, List.of()))
                    : perCitta;

            sb.append("Annunci disponibili sulla piattaforma:\n");

            datiRilevanti.forEach((citta, immobili) -> {
                if (immobili.isEmpty()) return;

                sb.append(String.format("\nCittà: %s (%d annunci)\n", citta, immobili.size()));

                Map<String, Long> perZona = immobili.stream()
                        .collect(Collectors.groupingBy(
                                r -> estraiZona(r.getAddress()),
                                Collectors.counting()
                        ));

                perZona.forEach((zona, count) ->
                        sb.append(String.format("  - Zona %s: %d immobili\n", zona, count))
                );

                OptionalDouble mqMedio = immobili.stream()
                        .mapToDouble(RealEstate::getSquareMetres)
                        .average();
                mqMedio.ifPresent(v ->
                        sb.append(String.format("  Media superficie: %.0f mq\n", v))
                );
            });

            return sb.toString();

        } catch (Exception e) {
            return "";
        }
    }

    private String chiamaGroq(List<Map<String, String>> messaggi) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", MODEL);
        body.put("messages", messaggi);
        body.put("max_tokens", 512);
        body.put("temperature", 0.3);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(GROQ_URL, request, Map.class);
            List<Map> choices = (List<Map>) response.getBody().get("choices");
            Map message = (Map) choices.get(0).get("message");
            return (String) message.get("content");
        } catch (Exception e) {
            return "Mi dispiace, si è verificato un errore. Riprova tra poco.";
        }
    }

    private String estraiCitta(String address) {
        try {
            String[] parti = address.split(",");
            if (parti.length > 1) {
                String secondaParte = parti[1].trim();
                return secondaParte.replaceAll("\\d{5}", "")
                        .replaceAll("\\(.*?\\)", "")
                        .trim();
            }
        } catch (Exception ignored) {}
        return address;
    }

    private String estraiZona(String address) {
        try {
            return address.split(",")[0].replaceAll("\\d+", "").trim();
        } catch (Exception ignored) {}
        return "Centro";
    }
}