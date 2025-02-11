package VTTPpaf.cheatsheet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import VTTPpaf.cheatsheet.model.Customer;
import VTTPpaf.cheatsheet.service.SQLservice;

@Controller
public class SQLcontroller {
    @Autowired
    private SQLservice sqLservice;

    //localhost:8080/getall
    @GetMapping(path= "/getall", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Customer>> getAllCustomers(){
        List<Customer> cusList = sqLservice.getAllCustomers();

        return ResponseEntity.status(200).body(cusList);
    }


    //http://localhost:8080/limitoffset?limit=2&offset=2
    @GetMapping("/limitoffset")
    @ResponseBody
    public ResponseEntity<List<Customer>> getAllCustomers(
        @RequestParam(name="limit", defaultValue = "3") int limit, //always pass a string as defaultvalue
        @RequestParam(name="offset", defaultValue = "0") int offset){
        List<Customer> cusList = sqLservice.getAllCustomers(limit, offset);

        return ResponseEntity.status(200).body(cusList);
    }

    //localhost:8080/get/2
    @GetMapping("/get/{customer_id}")
    @ResponseBody
    public ResponseEntity<Customer> getCustomerById(@PathVariable("customer_id") int id){
        Customer cus = sqLservice.getCustomerById(id);

        return ResponseEntity.status(200).body(cus);
    }

    //localhost:8080/delete/6
    @DeleteMapping("/delete/{customer_id}")
    public ResponseEntity<Boolean> deleteCustomerById(@PathVariable("customer_id") int id){
        boolean bool =  sqLservice.deleteCustomerById(id);

        return ResponseEntity.ok().body(bool);
    }

    //requestbody for json, requestparam for form
    /*  
        {
            "id":6,
            "fullname":"xdxd",
            "email":"xdxd@gamil.com"
        }

    */
    //localhost:8080/update + use above json and putmapping
    @PutMapping("/update")
    public ResponseEntity<Boolean> updateCustomerById(@RequestBody Customer customer){
        boolean bool =  sqLservice.updateCustomerById(customer);

        return ResponseEntity.ok().body(bool);
        
    }

    //localhost:8080/insert + use  above json and postmapping
    @PostMapping("/insert")
    public ResponseEntity<Boolean> insertCustomer(@RequestBody Customer customer){
        boolean bool =  sqLservice.insertCustomer(customer);

        return ResponseEntity.ok().body(bool);
        
    }



}
