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
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author mirandabeamer
 */
@CrossOrigin //allows our REST endpoints to accept cross-origin requests
@Controller
public class RESTController {
    private ContactListDao dao;
    
    @Inject
    public RESTController(ContactListDao dao){
        this.dao = dao;
    }
    
    //RETRIEVE CONTACT ENDPOINT
    @RequestMapping(value = "/contact/{id}", method = RequestMethod.GET)
    @ResponseBody
    //@PathVariable("id") long id tells the spring framework to extract the value associated with the path variable
    //and convert it to a long 
    public Contact getContact(@PathVariable("id") long id){
        return dao.getContactById(id);
    }
    
    //CREATE CONTACT ENDPOINT
    @RequestMapping(value="/contact", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED) //tells spring framework to return a 401 created 
    //http status code back with the response 
    @ResponseBody//tells spring framework to convert contact object to JSON, 
    //place it in the response body, and send to client
    public Contact createContact(@Valid @RequestBody Contact contact){
        return dao.addContact(contact);
    }
    
    //DELETE CONTACT ENDPOINT
    @RequestMapping(value= "/contact/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT) //void return type
    public void deleteContact(@PathVariable("id") long id){
        dao.removeContact(id);
    }
    
    //UPDATE CONTACT ENDPOINT
    @RequestMapping(value = "/contact/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateContact(@PathVariable("id") long id, @Valid @RequestBody Contact contact)
    throws UpdateIntegrityException {
        if(id != contact.getContactId()) {
            throw new UpdateIntegrityException("Contact Id on URL must match Contact ID in submitted data.");
        }
        //contact.setContactId(id);
        dao.updateContact(contact);
    }
    
    //GET ALL CONTACTS ENDPOINT
    @RequestMapping(value= "/contacts", method= RequestMethod.GET)
    @ResponseBody //indicates that something will be returned
    public List<Contact> getAllContacts() {
        return dao.getAllContacts();
    }
    
}
