package team2.lksh.p.formuland.activities

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import team2.lksh.p.formuland.R
import android.widget.ArrayAdapter
import android.support.v7.widget.SearchView.SearchAutoComplete
import team2.lksh.p.formuland.JsonDataProcessor


class MainActivity : AppCompatActivity(), MenuItemCompat.OnActionExpandListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        // Get ActionBar
//        val actionBar = supportActionBar
//        // Set below attributes to add logo in ActionBar.
//        actionBar!!.setDisplayShowHomeEnabled(true)
//        actionBar.setDisplayUseLogoEnabled(true)

//        val intent = Intent(this, FormulasListActivity::class.java)
//        startActivity(intent)
    }


//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.main_menu, menu)
//
//        // Get the search menu.
//        val searchMenu = menu.findItem(R.id.app_bar_menu_search)
//
//        // Get SearchView object.
//        val searchView = MenuItemCompat.getActionView(searchMenu) as SearchView
//
//        // Get SearchView autocomplete object.
//        val searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete
//        searchAutoComplete.setBackgroundColor(Color.BLUE)
//        searchAutoComplete.setTextColor(Color.GREEN)
//        searchAutoComplete.setDropDownBackgroundResource(android.R.color.holo_blue_light)
//
//        val jsonData = JsonDataProcessor(this)
//
//        val namesList = jsonData.getMenuData("all").names
//
//        // Create a new ArrayAdapter and add data to search auto complete object.
//        val newsAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, namesList)
//        searchAutoComplete.setAdapter(newsAdapter)
//
//        return true
//    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        return true
    }


}
