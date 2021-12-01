package com.example.foodieplanner

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
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
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SavedMealsFragment : Fragment() {
    private val model: Model by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private val albums = ArrayList<AlbumCard>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Dummy data
        albums.add(AlbumCard("Breakfest", Color.LTGRAY))
        albums.add(AlbumCard("Dinner", Color.GRAY))

        var view = inflater.inflate(R.layout.fragment_saved_meals, container, false)

        view.findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.saved_meals_toolbar).setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        // new meal floating action button clicked
        view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.savedmeals_new_meal_button).setOnClickListener {
            showDialog()
        }

        recyclerView = view.findViewById(R.id.saved_meals_albums_list)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        viewAdapter = AlbumnAdapter(albums)
        recyclerView.adapter = viewAdapter

        view.findViewById<Button>(R.id.saved_meals_all_meals_button).setOnClickListener {
            view.findNavController().navigate(R.id.action_savedMealsFragment_to_albumsFragment)
        }

        view.findViewById<Button>(R.id.add_album_button).setOnClickListener {
            val input = EditText(requireContext())
            input.setInputType(InputType.TYPE_CLASS_TEXT)

            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setTitle("Enter the album name")
                .setPositiveButton("Save") { dialog, id ->
                    val albumName: String = input.text.toString()
                    if (albumName != "") {
                        model.addAlbum(albumName)
                    }
                }
                .setNegativeButton("Cancel") { dialog, id -> }
                .setView(input)

            dialogBuilder.show()
        }

        return view
    }

    fun showDialog() {
        val fragmentManager = activity?.supportFragmentManager
        val newFragment = NewMealDialog()

        // The device is smaller, so show the fragment fullscreen
        val transaction = fragmentManager?.beginTransaction()
        // For a little polish, specify a transition animation
        transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
        if (transaction != null) {
            transaction.add(android.R.id.content, newFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}

class AlbumnAdapter(private val albumList: ArrayList<AlbumCard>):
    RecyclerView.Adapter<AlbumnAdapter.ViewHolder>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): AlbumnAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(
                R.layout.card_saved_meals_album,
                parent, false
            )
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindItems(albumList[position])
        }

        override fun getItemCount() = albumList.size

        class ViewHolder(private val view: View) :
            RecyclerView.ViewHolder(view) {
                fun bindItems(albumCard: AlbumCard) {
                    val title: TextView = itemView.findViewById(R.id.meal_card_title)
                    title.setBackgroundColor(albumCard.color)
                    title.text = albumCard.albumName

                    val image: ImageView = itemView.findViewById(R.id.album_card_image)
                    when (albumCard.albumName) {
                        "Breakfest" -> image.setImageResource(R.drawable.breakfast)
                        "Dinner" -> image.setImageResource(R.drawable.dinner)
                    }

                    itemView.setOnClickListener {
                        view.findNavController().navigate(R.id.action_savedMealsFragment_to_albumsFragment)
                    }
                }
            }
}