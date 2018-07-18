package team2.lksh.p.formuland.activities

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_formul_list.*
import kotlinx.android.synthetic.main.activity_main.*
import team2.lksh.p.formuland.adapters.SectionsCustomAdapter
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.MenuItemCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.formula_list_fragment.view.*
import team2.lksh.p.formuland.JsonDataProcessor
import team2.lksh.p.formuland.JsonTypes
import team2.lksh.p.formuland.R
import team2.lksh.p.formuland.adapters.generateFormulaActivity
import java.util.*

class FormulasListActivity : AppCompatActivity() {


    companion object {
        //List of subjects
        var subjectList = listOf<String>()
    }

    fun onItemClick(it: MenuItem): Boolean {
        when(it.itemId) {
            R.id.app_bar_menu_settings -> startActivity(Intent(this, SettingsActivity::class.java))
        }

        return true
    }

//    var currentSubject = JsonTypes.mathBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formul_list)

        val jsonData = JsonDataProcessor(this)

        val namesList = jsonData.getMenuData("all").names



        //Create adapter for fragment
        Log.i(JsonTypes.subtypes.size.toString(),JsonTypes.typeNameList.toString())
        subjectList = JsonTypes.typeNameList


        viewPager.adapter = SectionsPagerAdapter(supportFragmentManager)
        viewPager.addOnPageChangeListener(OnPageChange(this))

        setSupportActionBar(toolbar)
        // Get ActionBar
        val actionBar = supportActionBar
        // Set below attributes to add logo in ActionBar.
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayUseLogoEnabled(true)
        actionBar.title = JsonTypes.typeNameList[0]
    }

    class OnPageChange(private val activity: FormulasListActivity) : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            Log.d("Hello", JsonTypes.typeNameList[position])
            activity.supportActionBar?.title = JsonTypes.typeNameList[position]
        }

    }

    //Set toolbar's menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        // Get the search menu.
        val searchMenu = menu.findItem(R.id.app_bar_menu_search)
        val settingsMenu = menu.findItem(R.id.app_bar_menu_settings)

        settingsMenu.setOnMenuItemClickListener(::onItemClick)

        // Get SearchView object.
        val searchView = MenuItemCompat.getActionView(searchMenu) as SearchView

        // Get SearchView autocomplete object.
        val searchAutoComplete =
                searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)
                        as SearchView.SearchAutoComplete

        val jsonData = JsonDataProcessor(this)

        val menuData = jsonData.getMenuData("all")
        val namesList = menuData.names


        // Create a new ArrayAdapter and add data to search auto complete object.
        val newsAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, namesList)
        searchAutoComplete.setAdapter(newsAdapter)

        searchAutoComplete.setOnItemClickListener { parent, view, position, id ->

            val itemString = parent.getItemAtPosition(position) as String

            val index = findStr(itemString, namesList)
            val formulaId = menuData.idList[index]

            generateFormulaActivity(this, formulaId, "all")


        }

        return true
    }

    //It's adapter. It's need not to change
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return PlaceholderFragment.newInstance(position)
        }

        override fun getCount(): Int {
            return subjectList.size
        }
    }

    //Fragment
    class PlaceholderFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.formula_list_fragment, container, false)
            //For cast Context? to Context
            context?.let {
                with(rootView) {
                    //Create adapter for formulas list
                    val subject = nameFromNum(arguments?.getInt(ARG_SECTION_NAME))

                    //Create adapter for themes cards
                    section_tab.layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)
                    section_tab.adapter = SectionsCustomAdapter(subject, this, it)

                    list.layoutManager = LinearLayoutManager(it)
                }
            }
            return rootView
        }

        companion object {

            //Bundle name
            private const val ARG_SECTION_NAME = "SectionName"

            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NAME, sectionNumber)
                fragment.arguments = args
                return fragment
            }

            fun nameFromNum(i: Int?) = JsonTypes.typeCodeList[i ?: 0]
        }
    }
}
