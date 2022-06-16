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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author PAVILON GAMING
 */
@Controller
public class DummyController {

    DummyJpaController dummyController = new DummyJpaController();
    List<Dummy> data = new ArrayList<>();

    @RequestMapping("/read")

    public String getDummy(Model model) {
        int record = dummyController.getDummyCount();
        String result = "";
        try {
            data = dummyController.findDummyEntities().subList(0, record);
        } catch (Exception e) {
            result = e.getMessage();
        }
        model.addAttribute("goDummy", data);
        model.addAttribute("record", record);

        return "dummy";
    }

    @RequestMapping("/create")
    public String createDummy() {
        return "create";
    }

    @PostMapping(value = "/newData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public String newDummy(@RequestParam("gambar") MultipartFile file, HttpServletRequest data) throws ParseException, Exception {

        Dummy dumData = new Dummy();

        int id = Integer.parseInt(data.getParameter("id"));

        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(data.getParameter("tanggal"));
        byte[] image = file.getBytes();

        dumData.setId(id);
        dumData.setTanggal(date);
        dumData.setGambar(image);

        dummyController.create(dumData);

        return "redirect:/read";
    }

    @RequestMapping(value = "/image", method = RequestMethod.GET, produces = {MediaType.IMAGE_PNG_VALUE})

    public ResponseEntity<byte[]> getImg(@RequestParam("id") int id) throws Exception {
        Dummy dumData = dummyController.findDummy(id);
        byte[] img = dumData.getGambar();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(img);
    }

    @GetMapping("/delete/{id}")

    public String deleteDummy(@PathVariable("id") int id) throws Exception {
        dummyController.destroy(id);
        return "redirect:/read";
    }

    @RequestMapping("/edit/{id}")
    public String updateDummy(@PathVariable("id") int id, Model model) throws Exception {
        Dummy dumData = dummyController.findDummy(id);
        model.addAttribute("data", dumData);
        return "update";
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public String updateDummyData(HttpServletRequest data, @RequestParam("gambar") MultipartFile f, HttpServletRequest r) throws ParseException, Exception {
        Dummy dumData = new Dummy();

        int id = Integer.parseInt(r.getParameter("id"));
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(r.getParameter("tanggal"));
        byte[] image = f.getBytes();

        dumData.setId(id);
        dumData.setTanggal(date);
        dumData.setGambar(image);

        dummyController.edit(dumData);
        return "redirect:/read";
    }
}
