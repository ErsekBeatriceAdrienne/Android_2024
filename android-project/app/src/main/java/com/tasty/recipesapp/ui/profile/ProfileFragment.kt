package com.tasty.recipesapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tasty.recipesapp.R
import com.tasty.recipesapp.adapters.ProfileRecipeAdapter

class ProfileFragment : Fragment() {

    private lateinit var profileAdapter: ProfileRecipeAdapter
    private lateinit var recyclerView: RecyclerView
    //private lateinit var profileRepository: ProfileRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializáld a ProfileRepository-t
        //profileRepository = ProfileRepository(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        recyclerView = view.findViewById(R.id.favoriteRecipesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Betöltjük a kedvenc recepteket a JSON-ból
        //val favoriteRecipes = profileRepository.loadFavoriteRecipes()

        //profileAdapter = ProfileRecipeAdapter(favoriteRecipes, profileRepository)
        recyclerView.adapter = profileAdapter

        return view
    }
}
