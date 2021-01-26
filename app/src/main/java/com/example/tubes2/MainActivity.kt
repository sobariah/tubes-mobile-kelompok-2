package com.example.tubes2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubes2.fragment.InfoFragment
import com.example.tubes2.fragment.ProfileFragment
import com.example.tubes2.room.Constant
import com.example.tubes2.room.Note
import com.example.tubes2.room.NoteDB
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
//    private val profileFragment=ProfileFragment()
//    private val infoFragment=InfoFragment()
    var fragmentTransaction: FragmentTransaction? = null


    val db by lazy { NoteDB(this) }

    lateinit var noteAdapter: NoteAdapter

//    constructor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//    val infoFragment=InfoFragment()
//        val fragment= supportFragmentManager.findFragmentByTag((InfoFragment::class.java.simpleName))
//        if (fragment !is InfoFragment ){
//            supportFragmentManager.beginTransaction()
//                    .add(R.id.option,infoFragment,InfoFragment::class.java.simpleName)
//                    .commit()
//        }


        setupListener()
        setupRecyclerView()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.item1->{

//                option.setOnClickListener {
//                    val i = Intent(this@MainActivity, InfoFragment::class.java)
//                    startActivity(i)
//                }
                var fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction()
                        .add(R.id.option,InfoFragment())
                        .commit()
//                    onBackPressed()
                Toast.makeText(this,"About is Selected",Toast.LENGTH_SHORT).show()
            true
            }
//            R.id.item2->{
//                Toast.makeText(this,"Info is Selected",Toast.LENGTH_SHORT).show()
//                true
//            }
            else->
            {
                return super.onOptionsItemSelected(item)
            }
        }

    }


    override fun onStart() {
        super.onStart()
        loadNote()

    }
    fun loadNote(){
        CoroutineScope(Dispatchers.IO).launch {
            val notes= db.noteDao().getNotes()
            Log.d("MainActivity","dbResponse: $notes")
            withContext(Dispatchers.Main){
                noteAdapter.setData(notes)
            }
        }
    }


    fun setupListener(){
        button_create.setOnClickListener {
            startActivity(Intent(this,EditActivity::class.java))
            intentEdit(0,Constant.TYPE_CREATE)
        }
    }

    fun intentEdit(noteid:Int, intentType: Int){
        startActivity(
                Intent(applicationContext,EditActivity::class.java)
                        .putExtra("intent_id",noteid)
                        .putExtra("intent_type", intentType)
        )
    }

    private fun setupRecyclerView(){
        noteAdapter= NoteAdapter(arrayListOf(), object :NoteAdapter.OnAdapterListener{
            override fun onClick(note: Note) {
//                Toast.makeText(applicationContext, note.title, Toast.LENGTH_SHORT).show()
                intentEdit(note.id,Constant.TYPE_READ)
                startActivity(
                        Intent(applicationContext,EditActivity::class.java)
                                .putExtra("intent_id",note.id)
                )


            }

            override fun onUpdate(note: Note) {
                intentEdit(note.id,Constant.TYPE_UPDATE)
            }

            override fun onDelete(note: Note) {
                deleteDialog(note)
//                CoroutineScope(Dispatchers.IO).launch {
//                    db.noteDao().deleteNote(note)
//                loadNote()
//                }
            }
        })
        list_note.apply {
            layoutManager= LinearLayoutManager(applicationContext)
            adapter= noteAdapter
        }
    }
    private fun deleteDialog(note: Note){
        val alertDialog= AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Konfirmasi")
            setMessage("Yakin Hapus ${note.title}?")
            setNegativeButton("Batal") { dialogInterface, i ->
                dialogInterface.dismiss()

            }
            setPositiveButton("Hapus") { dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.noteDao().deleteNote(note)
                    loadNote()
                }
            }

            }
        alertDialog.show()
        }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}
