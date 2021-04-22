package kg.PerfectJob.BookStore.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import kg.PerfectJob.BookStore.entity.Media;
import kg.PerfectJob.BookStore.repository.MediaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

@Service
public class CloudinaryService {
    private final MediaRepository mediaRepository;

    public CloudinaryService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public Media createMediaFromMultipartFile(MultipartFile multipartFile) throws IOException {
        Media image = new Media();
        File file;
        try{
            file = Files.createTempFile(System.currentTimeMillis() + "",
                    Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().length()-4)) // .jpg
                    .toFile();
            multipartFile.transferTo(file);

            String urlKey = "cloudinary://122578963631996:RKDo37y7ru4nnuLsBGQbwBUk65o@zhazgul/";
            Cloudinary cloudinary = new Cloudinary(urlKey);
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            image.setName((String) uploadResult.get("public_id"));
            image.setUrl((String) uploadResult.get("url"));
            image.setFormat((String) uploadResult.get("format"));
            return mediaRepository.save(image);
        } catch (IOException e){
            throw new IOException("Unable to set image to author\n" + e.getMessage());
        }
    }
}
