/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.contactlistspringmvc.controller;

import com.sg.contactlistspringmvc.dao.ContactListDao;
import com.sg.contactlistspringmvc.model.Contact;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author mirandabeamer
 */
@Controller
public class ContactController {
    
    ContactListDao dao;
    
    @Inject //anotation for dependency injection 
    public ContactController(ContactListDao dao) {
        this.dao = dao;
    }
    
    @RequestMapping(value="/displayContactsPage", method=RequestMethod.GET)
    public String displayContactsPage(Model model){
        List<Contact> contactList = dao.getAllContacts();
        model.addAttribute("contactList", contactList);
        return "contacts";
    }
    
    @RequestMapping(value = "/createContact", method = RequestMethod.POST)
        public String createContact(@Valid HttpServletRequest request) {
        // grab the incoming values from the form and create a new Contact
        // object
        Contact contact = new Contact();
        contact.setFirstName(request.getParameter("firstName"));
        contact.setLastName(request.getParameter("lastName"));
        contact.setCompany(request.getParameter("company"));
        contact.setPhone(request.getParameter("phone"));
        contact.setEmail(request.getParameter("email"));

        // persist the new Contact
                dao.addContact(contact);
        // we don't want to forward to a View component - we want to
        // redirect to the endpoint that displays the Contacts Page
        // so it can display the new Contact in the table.
        return "redirect:displayContactsPage";
    }
        
    @RequestMapping(value="/displayContactDetails", method= RequestMethod.GET)
    public String displayContactDetails(HttpServletRequest request, Model model){
        String contactIdParameter = request.getParameter("contactId");
        int contactId = Integer.parseInt(contactIdParameter);
        Contact contact = dao.getContactById(contactId);
        model.addAttribute("contact", contact);
        return "contactDetails";
    }
    
    @RequestMapping(value="/deleteContact", method = RequestMethod.GET)
    //get because the request is sent as a result of clicking on a link 
    public String deleteContact(HttpServletRequest request){
        String contactIdParameter = request.getParameter("contactId");
        long contactId = Long.parseLong(contactIdParameter);
        dao.removeContact(contactId);
        //important to refresh the page to reflect changes 
        return "redirect:displayContactsPage";
    }
    
    //first controller endpoint for edit contact - similar to get contact details
    @RequestMapping(value="/displayEditContactForm", method=RequestMethod.GET)
    public String displayEditContactForm(HttpServletRequest request, Model model){
        String contactIdParameter = request.getParameter("contactId");
        long contactId = Long.parseLong(contactIdParameter);
        Contact contact = dao.getContactById(contactId);
        model.addAttribute("contact", contact);
        return "editContactForm";
    }
    
    //second controller endpoint for edit contact
    @RequestMapping(value = "/editContact", method = RequestMethod.POST)
    //@valid indicates incoming contact object must be validated 
    public String editContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result) {
        //binding results allows us to check for validation erros
        if(result.hasErrors()){
            return "editContactForm";
            //validation errors will show up on the form
        }
        
        dao.updateContact(contact);
        return "redirect:displayContactsPage";
    }   
}
