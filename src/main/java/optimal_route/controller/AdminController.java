package optimal_route.controller;

import optimal_route.contract.Account;
import optimal_route.contract.IAccountPersistency;
import optimal_route.view.AdminView;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;

public class AdminController {
    private AdminView view;
    private IAccountPersistency accountsPersistency;
    public AdminController(AdminView view, IAccountPersistency accountsPersistency){
        this.view=view;
        this.accountsPersistency=accountsPersistency;
        view.addAddListener(new AddEmployeeListener());
        view.addRemoveListener(new RemoveEmployeeListener());
        view.addUpdateListener(new UpdateEmployeeListener());
        view.addListListener(new ListListener());
        List<Account> employeeList = null;
        try {
            employeeList = accountsPersistency.getAll();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        for (Account acc : employeeList) {
            if (!acc.getRole().equals("admin")) {
                ((DefaultListModel<String>)view.getList().getModel()).addElement(acc.getUsername());
            }
        }

    }
    class ListListener implements ListSelectionListener{

        @Override
        public void valueChanged(ListSelectionEvent e) {
            List<Account> accs= null;
            try {
                accs = accountsPersistency.getAll();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
            Account acc=null;
            DefaultListModel<String> model = (DefaultListModel<String>) view.getList().getModel();
            for(Account account:accs){
                if(account.getUsername().equals(view.getList().getSelectedValue())){
                    acc=account;
                    break;
                }
            }
            if(acc!=null) {
                view.getT1().setText(acc.getRole());
                view.getT2().setText(acc.getUsername());
                view.getT3().setText(acc.getEmail());
                view.getT4().setText(acc.getUsername());
                view.getT5().setText(acc.getPswd());
            }
        }
    }
    class UpdateEmployeeListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            List<Account> accs= null;
            try {
                accs = accountsPersistency.getAll();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
            Account acc=null;
            DefaultListModel<String> model = (DefaultListModel<String>) view.getList().getModel();
            for(Account account:accs){
                if(account.getUsername().equals(view.getList().getSelectedValue())){
                    acc=account;
                    break;
                }
            }
            if(acc!=null) {
                acc.setRole(view.getT1().getText());
                acc.setName(view.getT2().getText());
                acc.setEmail(view.getT3().getText());
                acc.setUsername(view.getT4().getText());
                acc.setPswd(view.getT5().getText());
                try {
                    accountsPersistency.update(acc);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    class RemoveEmployeeListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            List<Account> accs= null;
            try {
                accs = accountsPersistency.getAll();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
            Account acc=null;
            DefaultListModel<String> model = (DefaultListModel<String>) view.getList().getModel();
            for(Account account:accs){
                if(account.getUsername().equals(view.getList().getSelectedValue())){
                    acc=account;
                    break;
                }
            }
            if(model.contains(acc)){
                model.removeElement(acc);
                view.getList().setModel(model);
            }
            try {
                accountsPersistency.delete(acc.getId());
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
    }
    class AddEmployeeListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultListModel<String> model = (DefaultListModel<String>) view.getList().getModel();
            List<Account> listm = null;
            try {
                listm = accountsPersistency.getAll();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
            Account usrAcc = new Account.AccountBuilder().role(view.getT1().getText()).email(view.getT3().getText()).pswd(view.getT5().getText()).username(view.getT4().getText()).build();
            boolean found = false;
            for (Account account : listm) {
                if (account.equals(usrAcc)) {
                    found = true;
                    break;
                }
            }
            if (found == false) {
                listm.add(usrAcc);
                model.addElement(usrAcc.getUsername());
                view.getList().setModel(model);
                try {
                    accountsPersistency.insert(usrAcc);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
