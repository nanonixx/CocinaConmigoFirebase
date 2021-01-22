package com.puig.proyecto;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.puig.proyecto.databinding.FragmentRecetaBinding;


public class RecetaFragment extends Fragment {
        private FragmentRecetaBinding binding;
        private NavController navController;
        private RecetasViewModel recetasViewModel;


        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return (binding = com.puig.proyecto.databinding.FragmentRecetaBinding.inflate(inflater, container, false)).getRoot();
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recetasViewModel = new ViewModelProvider(requireActivity()).get(RecetasViewModel.class);
        navController = Navigation.findNavController(view);

        recetasViewModel.seleccionado().observe(getViewLifecycleOwner(), receta -> {
            binding.nombrereceta.setText(receta.nombreReceta);
        });

        binding.sharebutton.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Mis recetingas te las envío");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });
    }
}
