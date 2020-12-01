/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.contactlistspringmvc.dao;

import com.sg.contactlistspringmvc.model.Contact;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author mirandabeamer
 */
public class ContactListDaoInMemImpl implements ContactListDao{
    //holds all contact objects to stimulate the database 
    private Map<Long, Contact> contactMap = new HashMap<>();
    //used to assign id to contacts
    private static long contactIdCounter = 0;

    @Override
    public Contact addContact(Contact contact) {
        contact.setContactId(contactIdCounter);
        contactIdCounter++;
        contactMap.put(contact.getContactId(), contact);
        return contact;
    }

    @Override
    public void removeContact(long contactId) {
        contactMap.remove(contactId);
    }
    
    @Override
    public void updateContact(Contact contact) {
        contactMap.put(contact.getContactId(), contact);
    }

    @Override
    public List<Contact> getAllContacts() {
        Collection<Contact> c = contactMap.values();
        return new ArrayList(c);
    }

    @Override
    public Contact getContactById(long contactId) {
        return contactMap.get(contactId);
    }

    @Override
    public List<Contact> searchContacts(Map<SearchTerm, String> criteria) {
        //get all serach term values from the map
        String firstNameSearchCriteria = criteria.get(SearchTerm.FIRST_NAME);
        String lastNameSearchCriteria = criteria.get(SearchTerm.LAST_NAME);
        String companySearchCriteria = criteria.get(SearchTerm.COMPANY);
        String phoneSearchCriteria = criteria.get(SearchTerm.PHONE);
        String emailSearchCriteria = criteria.get(SearchTerm.EMAIL);
        
        //Declare all predicate conditions (predicate = functional interface with one method 
        //called test(T t) that returns a boolean 
        Predicate<Contact> firstNameMatchPredicate;
        Predicate<Contact> lastNameMatchPredicate;
        Predicate<Contact> companyMatchPredicate;
        Predicate<Contact> phoneMatchPredicate;
        Predicate<Contact> emailMatchPredicate;
        
        //placeholder predicate - always returns true - used for empty search terms
        Predicate<Contact> truePredicate = (c) -> {
            return true;
        };
        
        //assign values to predicates - if search term is empty, assign the default truepredicate
        if(firstNameSearchCriteria ==null || firstNameSearchCriteria.isEmpty()){
            firstNameMatchPredicate = truePredicate;
        } else{
            firstNameMatchPredicate = (c) -> c.getFirstName().equals(firstNameSearchCriteria);
        }
        
        if(lastNameSearchCriteria ==null || lastNameSearchCriteria.isEmpty()){
            lastNameMatchPredicate = truePredicate;
        } else{
            lastNameMatchPredicate = (c) -> c.getLastName().equals(lastNameSearchCriteria);
        }
        
        if(companySearchCriteria ==null || companySearchCriteria.isEmpty()){
            companyMatchPredicate = truePredicate;
        } else{
           companyMatchPredicate = (c) -> c.getCompany().equals(companySearchCriteria);
        }
        
        if(phoneSearchCriteria ==null || phoneSearchCriteria.isEmpty()){
            phoneMatchPredicate = truePredicate;
        } else{
            phoneMatchPredicate = (c) -> c.getPhone().equals(phoneSearchCriteria);
        }
        
        if(emailSearchCriteria ==null || emailSearchCriteria.isEmpty()){
            emailMatchPredicate = truePredicate;
        } else{
            emailMatchPredicate = (c) -> c.getEmail().equals(emailSearchCriteria);
        }
        
        //return the list of contacts that match the given criteria, 
        //AND all the predicates together in a filter operation 
        return contactMap.values().stream()
                .filter(firstNameMatchPredicate
                        .and(lastNameMatchPredicate)
                        .and(companyMatchPredicate)
                        .and(phoneMatchPredicate)
                        .and(emailMatchPredicate))
                .collect(Collectors.toList());
    }

    
}
