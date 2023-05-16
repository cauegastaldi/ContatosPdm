package br.edu.ifsp.contatopdm.controller

import androidx.room.Room
import br.edu.ifsp.contatopdm.model.Contact
import br.edu.ifsp.contatopdm.model.ContactDao
import br.edu.ifsp.contatopdm.model.ContactDaoRoom
import br.edu.ifsp.contatopdm.model.ContactDaoSqlite
import br.edu.ifsp.contatopdm.view.MainActivity

class ContactController(private val mainActivity: MainActivity) {
    //private val contactDaoImpl: ContactDao = ContactDaoSqlite(mainActivity)
    private val contactDaoImpl: ContactDao = Room.databaseBuilder(
        mainActivity, ContactDaoRoom::class.java, ContactDaoRoom.CONTACT_DATABASE_FILE
    ).build().getContactDao()

    fun insertContact(contact: Contact) {
        Thread {
            contactDaoImpl.createContact(contact)
        }.start()
    }
    fun getContact(id: Int) = contactDaoImpl.retrieveContact(id)
    fun getContacts() {
        Thread {
            val list = contactDaoImpl.retrieveContacts()
            mainActivity.runOnUiThread {
                mainActivity.updateContactList(list)
            }
        }.start()
    }
    fun editContact(contact: Contact) {
        Thread {
            contactDaoImpl.updateContact(contact)
        }.start()
    }
    fun removeContact(contact: Contact) {
        Thread {
            contactDaoImpl.deleteContact(contact)
        }.start()
    }
}