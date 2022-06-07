/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unu.jogja.project.ktp.coba;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 * @author PAVILON GAMING
 */

@Controller
public class DummyController {

    DummyJpaController dummyController = new DummyJpaController();
    List<Dummy> data = new ArrayList<>();
    
    @RequestMapping("/read")
    //@ResponseBody
    public String getDummy(Model model) {
        int record = dummyController.getDummyCount();
        String result = "";
        try {
            data = dummyController.findDummyEntities().subList(0, record);
        } catch (Exception e) {
            result = e.getMessage();
        }

        model.addAttribute("godummy", data);
        model.addAttribute("record", record);

        return "dummy";
    }

    @RequestMapping("/create")
    public String createDummy() {
        return "dummy/create";
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //@ResponseBody

    public String newDummy( HttpServletRequest data, @RequestParam("gambar") MultipartFile file, HttpServletResponse response) throws ParseException, Exception {
        
        Dummy dumdata = new Dummy();

        String id = data.getParameter("id");
        int iid = Integer.parseInt(id);

        String tanggal = data.getParameter("tanggal");
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);

        //String filename = StringUtils.cleanPath(file.getOriginalFilename());
        byte[] image = file.getBytes();
        dumdata.setId(iid);
        dumdata.setTanggal(date);
        dumdata.setGambar(image);
        dummyController.create(dumdata);


        response.sendRedirect("/read");
        return "redirect:/read";
    }
    
    @RequestMapping(value = "/gambar", method = RequestMethod.GET, produces = {MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> getImg(@RequestParam("id") int id) throws Exception {
        Dummy dum = dummyController.findDummy(id);
        byte[] gambar = dum.getGambar();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(gambar);

    }
}
