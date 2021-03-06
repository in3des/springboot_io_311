package com.example.springboot_io_311.controllers;

import com.example.springboot_io_311.entity.Person;
import com.example.springboot_io_311.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;


@Controller
@RequestMapping("/")
public class PeopleController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("/admin")
    public String showIndexPage(Model model, Principal principal) {
        System.out.println("simple - " + principal.getName());
        model.addAttribute("people", peopleService.index());
        return "people/index";
    }

    @GetMapping("/user")
    public String showOneUserPage(Model model, Authentication authentication) {
        model.addAttribute("person", peopleService.findPersonByEmail(((Person) authentication.getPrincipal()).getEmail()));
        return "people/show";
    }

    @GetMapping("/admin/new")
    public String showNewPersonPage(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping("/admin")
    public String createPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "people/new";
        }

        peopleService.save(person);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/edit")
    public String showEditPersonPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("person", peopleService.show(id));
        return "people/edit";
    }

//    @PatchMapping("/admin/{id}")
    @PostMapping("/admin/{id}")
    public String UpdatePerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") Long id) {

        if (bindingResult.hasErrors()) {
            return "people/edit";
        }

        peopleService.update(person, id);
        return "redirect:/admin";
    }

//    @DeleteMapping("admin/{id}")
    @PostMapping("/admin/{id}/delete")
    public String deletePerson(@PathVariable("id") Long id) {
        peopleService.delete(id);
        return "redirect:/admin";
    }


    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

}
