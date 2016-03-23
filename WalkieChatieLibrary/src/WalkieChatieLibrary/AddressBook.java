/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WalkieChatieLibrary;

import DataContract.Contact;
import DataContract.DataTypes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author AndyChen
 */
public class AddressBook {
    public Map<String, Contact> map;

    public AddressBook()
    {
        map = new HashMap<>();
        this.listeners = new ArrayList<>();
    }
    
    public Contact Lookup(String userName)
    {
        return map.get(userName);
    }
    
    public Contact add(Contact contact)
    {
        if (contact == null || contact.getName() == null)
            return null;
        
        return map.put(contact.getName(), contact);
    }
    private boolean remove(Contact contact)
    {
        return map.remove(contact.getName())!=null;
    }
    
    private boolean remove(String userName)
    {
        return map.remove(userName)!=null;
    }
    
    public boolean updateStatus(String userName, boolean isOnline)
    {
        Contact user = Lookup(userName);
        if (user == null)
        {// new user
            add(new Contact(userName, "", 0, 0, isOnline));
            
            raiseUserStatusChangeEvent(userName, isOnline);
        }
        else
        {// old user
            boolean orgStatus = user.getIsOnline();

            if (orgStatus != isOnline) {
                user.setIsOnline(isOnline);
                raiseUserStatusChangeEvent(userName, isOnline);
            }
        }
        
        return null != user;
    }
    
    private final List<DataTypes.UserListListener> listeners;

    public void addUserListListener(DataTypes.UserListListener listener) {
        listeners.add(listener);
    }
    
    private void raiseUserStatusChangeEvent(String userName, boolean isOnline)
    {
        for (DataTypes.UserListListener item : listeners) {
            item.userStatusChanged(userName, isOnline);
        }
    }
}
