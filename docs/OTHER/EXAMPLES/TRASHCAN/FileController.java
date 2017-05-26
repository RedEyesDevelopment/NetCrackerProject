package projectpackage.controllers;

import irepdata.model.Image;
import irepdata.service.ImageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by Gvozd on 12.12.2016.
 */
@Controller
public class FileController {
    public static final String URLCLASSPREFIX = "/fileapi/";
    private final static Logger logger = Logger.getLogger(FileController.class);

    @Autowired
    private ImageService imageService;

    //FILE UPLOAD PAGE
    @RequestMapping(URLCLASSPREFIX + "fileupload")
    public String fileUploadPAge() {
        return "fileupload";
    }

    //FILE UPLOAD HANDLER
    @RequestMapping(value = URLCLASSPREFIX + "uploadFile", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file, String publicity, HttpServletRequest request, HttpServletResponse response) {

        Boolean publicitys = Boolean.parseBoolean(publicity);
        String name = null;
        if (publicitys == null) {
            publicitys = false;
        }
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                name = file.getOriginalFilename();

                StringBuilder rootPath = new StringBuilder(request.getServletContext().getRealPath("/").toString());
                rootPath.append("/dynamic/");
                System.out.println(rootPath.toString());
                File dir = new File(rootPath.toString() + File.separator);

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                System.out.println("in file controller");

                File uploadedFile = new File(dir.getAbsolutePath() + File.separator + name);

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
                stream.write(bytes);
                stream.flush();
                stream.close();

                logger.info("uploaded: " + uploadedFile.getAbsolutePath());

                String fileName = uploadedFile.getName();
                Long userId = (Long) request.getSession().getAttribute("USER_ID");
                System.out.println(userId.toString());

                Image image = new Image();
                image.setImageName(fileName);
                image.setImageAuthorId(userId);
                image.setPosted(new Timestamp(System.currentTimeMillis()));
                if (!publicity.equals("")) {
                    image.setPublicity(publicitys);
                } else image.setPublicity(false);
                imageService.createImage(image);
                String redirect = "redirect:/ideas/cabinet";
                return redirect;

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
        return "ERROR";
    }

    //FILE GALLERY PAGE
    @RequestMapping(URLCLASSPREFIX + "filelist&show={offset}")
    public String getFirstFilesList(@PathVariable("offset") Long offset, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
        Long offsetStep = offset;
        List<Image> imglist = imageService.getImages(offsetStep, true);
        Long imagesCount = imageService.getImageCount();
        request.removeAttribute("NEXTFILES");
        request.removeAttribute("ISNEXTFILES");
        request.removeAttribute("PREVFILES");
        request.removeAttribute("IPREVFILES");

        System.out.println("offsetStep is "+offsetStep);
        System.out.println("images count is "+imagesCount);
        System.out.println("image list size is "+imglist.size());

        if (offsetStep< ( imagesCount-Image.MAXIMAGESSHOWINGCAPACITY)){
            Long offsetForNext = offsetStep + Image.MAXIMAGESSHOWINGCAPACITY;
            request.setAttribute("NEXTFILES", offsetForNext);
            request.setAttribute("ISNEXTFILES", true);
            System.out.println("NEXTFILES is "+offsetForNext);
        }
        if (offsetStep>=Image.MAXIMAGESSHOWINGCAPACITY){
            Long offsetForPrev = offsetStep - Image.MAXIMAGESSHOWINGCAPACITY;
            request.setAttribute("PREVFILES", offsetForPrev);
            request.setAttribute("ISPREVFILES", true);
            System.out.println("PREVFILES is "+offsetForPrev);

        }
        map.put("imageList", imglist);
        return "fileslist";
    }

}

