package com.example.foodieplanner

import android.app.AlertDialog
import android.content.res.Configuration
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class SavedMealsFragment : Fragment() {
    private val model: Model by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private var viewAdapter = AlbumAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_saved_meals, container, false)

        recyclerView = view.findViewById(R.id.saved_meals_albums_list)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = viewAdapter

        // make adapter observe view model
        model.albums.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewAdapter.data = it
            }
        })

        // For deleting albums
        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewAdapter.removeAlbum(viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // Load albums from firebase
//        model.database.child("Albums").get().addOnSuccessListener { data ->
//            for (album in data.children) {
//                (viewAdapter as AlbumAdapter).insertAlbum(album.value.toString())
//            }
//        }

        view.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.saved_meals_toolbar).setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        // Add a meal
        if (activity?.resources?.configuration?.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.savedmeals_new_meal_button).setOnClickListener {
                showDialog()
            }
        }

        // View all meals
        if (activity?.resources?.configuration?.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            view.findViewById<Button>(R.id.saved_meals_all_meals_button).setOnClickListener {
                (viewAdapter as AlbumAdapter).clearAlbumList()
                view.findNavController().navigate(R.id.action_savedMealsFragment_to_albumsFragment,
                    bundleOf("albumName" to "All_"))
            }
        }

        // Add an album
        if (activity?.resources?.configuration?.orientation != Configuration.ORIENTATION_LANDSCAPE) {
            view.findViewById<Button>(R.id.add_album_button).setOnClickListener {
                val input = EditText(requireContext())
                input.setInputType(InputType.TYPE_CLASS_TEXT)

                val dialogBuilder = AlertDialog.Builder(requireContext())
                dialogBuilder.setTitle("Enter the album name")
                    .setPositiveButton("Save") { dialog, id ->
                        val albumName: String = input.text.toString()
                        if (albumName != "") {
                            //(viewAdapter as AlbumAdapter).insertAlbum(albumName)
                            model.addAlbum(albumName)
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, id -> }
                    .setView(input)

                dialogBuilder.show()
            }
        }

        return view
    }

    private fun showDialog() {
        val fragmentManager = activity?.supportFragmentManager
        val newFragment = NewMealDialog()

        val transaction = fragmentManager?.beginTransaction()
        transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction?.add(android.R.id.content, newFragment)?.addToBackStack(null)?.commit()
    }

    inner class AlbumAdapter(): RecyclerView.Adapter<AlbumAdapterViewHolder>() {

        var data = arrayListOf<String>()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumAdapterViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.card_saved_meals_album, parent, false)
            return AlbumAdapterViewHolder(view)
        }

        override fun onBindViewHolder(holder: AlbumAdapterViewHolder, position: Int) {
            holder.title.text = data.get(position)

            holder.image.setBackgroundColor(getRandomColor())

            holder.itemView.setOnClickListener {
                it.let {
                    it.findNavController().navigate(R.id.action_savedMealsFragment_to_albumsFragment,
                        bundleOf("albumName" to data.get(position)))
                }
            }

        }

//        fun insertAlbum(album: String) {
//            model.addAlbum(album)
//        }

        fun removeAlbum(pos: Int) {
            model.deleteAlbum(data[pos])
            data.removeAt(pos)
            notifyItemRemoved(pos)
            notifyItemRangeChanged(pos, data.size)
        }

        fun clearAlbumList() {
            data.clear()
        }

        private fun getRandomColor(): Int {
            val rand = Random.nextInt(0, 3)
            when (rand) {
                0 -> return R.color.softblue
                1 -> return R.color.rosyhiglight
                2 -> return R.color.brewedmustard
                3 -> return R.color.creamypeach
            }
            return R.color.grisilla
        }

        override fun getItemCount() = data.size

    }

    //TODO Remove
    inner class AlbumAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title: TextView = itemView.findViewById(R.id.meal_card_title)
            val image: ImageView = itemView.findViewById(R.id.album_card_image)
    }
}
