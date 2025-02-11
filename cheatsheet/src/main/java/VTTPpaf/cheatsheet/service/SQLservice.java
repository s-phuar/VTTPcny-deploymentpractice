package VTTPpaf.cheatsheet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import VTTPpaf.cheatsheet.model.Customer;
import VTTPpaf.cheatsheet.repository.SQLrepository;

@Service
public class SQLservice {
    @Autowired
    private SQLrepository sqLrepository;

    //SELECT * FROM customer
    public List<Customer> getAllCustomers(){
        List<Customer> cusList = sqLrepository.getAllCustomers();

        return cusList;
    }

    //SELECT * FROM customer LIMIT ? OFFSET ?
    public List<Customer> getAllCustomers(int limit, int offset){
        List<Customer> cusList = sqLrepository.getAllCustomers(limit, offset);

        return cusList;
    }

    //SELECT * FROM customer where id = ?;
    public Customer getCustomerById(int id){
        Customer cus = sqLrepository.getCustomerById(id);
        return cus;
    }

    //DELETE FROM customer where id = ?
    public boolean deleteCustomerById(int id){
        return sqLrepository.deleteCustomerById(id);
    }

    //UPDATE customer set fullname = ?, email = ? WHERE id = ?
    public boolean updateCustomerById(Customer customer){
        return sqLrepository.updateCustomerById(customer);
    }

    //INSERT INTO customer (fullname, email) values (?, ?)
    public boolean insertCustomer(Customer customer){
        return sqLrepository.insertCustomer(customer);
    }


}
