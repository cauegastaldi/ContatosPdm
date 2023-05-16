package br.edu.ifsp.contatopdm.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.contatopdm.R
import br.edu.ifsp.contatopdm.adapter.ContactAdapter
import br.edu.ifsp.contatopdm.adapter.ContactRvAdapter
import br.edu.ifsp.contatopdm.adapter.OnContactClickListener
import br.edu.ifsp.contatopdm.controller.ContactController
import br.edu.ifsp.contatopdm.databinding.ActivityMainBinding
import br.edu.ifsp.contatopdm.model.Contact

class MainActivity : BaseActivity(), OnContactClickListener {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Data source
    private val contactList: MutableList<Contact> = mutableListOf()

    // Adapter
    private val contactAdapter: ContactRvAdapter by lazy {
        ContactRvAdapter(contactList, this)
    }

    private lateinit var carl: ActivityResultLauncher<Intent>

    // Controller
    private val contactController: ContactController by lazy {
        ContactController(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)
        supportActionBar?.subtitle = getString(R.string.contact_list)

        contactController.getContacts()
        amb.contactsRv.layoutManager = LinearLayoutManager(this)
        amb.contactsRv.adapter = contactAdapter

        carl = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val contact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra(EXTRA_CONTACT, Contact::class.java)
                } else {
                    result.data?.getParcelableExtra<Contact>(EXTRA_CONTACT)
                }
                contact?.let{_contact ->
                    val position = contactList.indexOfFirst { it.id == _contact.id }
                    if (position != -1) {
                        contactList[position] = _contact
                        contactController.editContact(_contact)
                        Toast.makeText(this, "Contato editado!", Toast.LENGTH_SHORT).show()
                    } else {
                        contactController.insertContact(_contact)
                        Toast.makeText(this, "Contato adicionado!", Toast.LENGTH_SHORT).show()
                    }
                    contactController.getContacts()
                    contactAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addContactMi -> {
                carl.launch(Intent(this, ContactActivity::class.java))
                true
            }
            else -> false
        }
    }

    fun updateContactList(_contactList: MutableList<Contact>) {
        contactList.clear()
        contactList.addAll(_contactList)
        contactAdapter.notifyDataSetChanged()
    }

    // Funções que serão chamadas sempre que uma célula for clicada no RecyclerView.
    // A associação entre célula e função será feita no ContactRvAdapter.
    override fun onTileContactClick(position: Int) {
        val contact = contactList[position]
        val contactIntent = Intent(this@MainActivity, ContactActivity::class.java)
        contactIntent.putExtra(EXTRA_CONTACT, contact)
        contactIntent.putExtra(EXTRA_VIEW_CONTACT, true)
        startActivity(contactIntent)
    }

    override fun onEditMenuItemClick(position: Int) {
        val contact = contactList[position]
        val contactIntent = Intent(this, ContactActivity::class.java)
        contactIntent.putExtra(EXTRA_CONTACT, contact)
        carl.launch(contactIntent)
    }

    override fun onRemoveMenuItemClick(position: Int) {
        val contact = contactList[position]
        contactList.removeAt(position)
        contactController.removeContact(contact)
        contactAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Contato removido!", Toast.LENGTH_SHORT).show()
    }
}