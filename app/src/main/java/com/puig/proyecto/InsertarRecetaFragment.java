package com.puig.proyecto;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.puig.proyecto.databinding.FragmentInsertarRecetaBinding;
import com.puig.proyecto.databinding.FragmentListaRecetasBinding;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static androidx.core.content.ContextCompat.checkSelfPermission;


public class InsertarRecetaFragment extends Fragment {

    private FragmentInsertarRecetaBinding binding;
    private RecetasViewModel recetasViewModel;
    Uri imagenSeleccionada;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentInsertarRecetaBinding.inflate(inflater, container, false)).getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recetasViewModel = new ViewModelProvider(requireActivity()).get(RecetasViewModel.class);
        NavController navController = Navigation.findNavController(view);

        binding.previsualizarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
                    abrirGaleria();
                } else {
                    lanzadorPermisos.launch(READ_EXTERNAL_STORAGE);
                }
            }
        });

        recetasViewModel.imagenSeleccionada.observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                imagenSeleccionada = uri;
                Glide.with(requireView()).load(uri).into(binding.previsualizarFoto);
            }
        });

        binding.insertar.setOnClickListener(v -> {
            String nombre = binding.nombre.getText().toString();

           recetasViewModel.insertar(nombre, imagenSeleccionada.toString());
           navController.popBackStack();
           recetasViewModel.establecerImagenSeleccionada(null);

        });

        
}

    private final ActivityResultLauncher<String> lanzadorPermisos =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    abrirGaleria();
                }
            });

    private final ActivityResultLauncher<String> lanzadorGaleria =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri ->
            recetasViewModel.establecerImagenSeleccionada(uri));
            //Glide.with(requireView()).load(uri).into(binding.previsualizarFoto));


    private void abrirGaleria(){
        lanzadorGaleria.launch("image/*");
    }
}