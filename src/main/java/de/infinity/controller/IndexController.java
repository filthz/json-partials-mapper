package de.infinity.controller;

import de.infinity.domain.SoftwareFile;
import de.infinity.parser.UriOptions;
import de.infinity.repository.FileRepository;
import de.infinity.util.JsonMappingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private FileRepository fileRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public String index() {

        SoftwareFile file = fileRepository.findByName("test");
        if(file == null) {
            file = new SoftwareFile();
            file.setName("test");
            file.setContent("Testinhalt".getBytes());
            file.setCreatedAt(new Date());

            fileRepository.save(file);
        }

        return "Welcome to JONMaper!";
    }

    @RequestMapping(value = "/file/{parameter}", method = RequestMethod.GET)
    @ResponseBody
    public List<SoftwareFile> getAllFilter(@PathVariable String parameter) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        List<SoftwareFile> boxSoftware = fileRepository.findAll();

        JsonMappingFilter jsonMappingFilter = new JsonMappingFilter();
        jsonMappingFilter.filter(boxSoftware, UriOptions.parse(parameter));

        return boxSoftware;
    }

    @RequestMapping(value = "/file/", method = RequestMethod.GET)
    @ResponseBody
    public List<SoftwareFile> getAll() {
        return fileRepository.findAll();
    }
}
