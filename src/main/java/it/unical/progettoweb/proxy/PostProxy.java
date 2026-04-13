package it.unical.progettoweb.proxy;

import it.unical.progettoweb.dao.impl.PhotoDao;
import it.unical.progettoweb.model.Photo;
import it.unical.progettoweb.model.Post;

import java.util.List;

public class PostProxy extends Post {
    private final PhotoDao photoDao;
    private boolean isPhotosCaches = false;

    public PostProxy(PhotoDao photoDao) {
        this.photoDao = photoDao;
    }

    public List<Photo> getPhotos() {
        if (!isPhotosCaches && super.getId() != null) {
            List<Photo> photosCaches = photoDao.getByPostId(getId());
            super.setPhotos(photosCaches);
            isPhotosCaches = true;

        }
        return super.getPhotos();
    }

}
