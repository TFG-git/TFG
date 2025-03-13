package com.example.tfg_inicial;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.tfg_inicial.adaptadores.AdaptadorPersonalizadoCarteleras;
import com.example.tfg_inicial.clases.Cartelera;
import com.example.tfg_inicial.clases.Pelea;
import com.example.tfg_inicial.clases.Peleador;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link inicio_FragmentCarteleras#newInstance} factory method to
 * create an instance of this fragment.
 */
public class inicio_FragmentCarteleras extends Fragment {

    private ListView lvCarteleras;
    private ArrayList<Cartelera> carteleras;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public inicio_FragmentCarteleras() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment inicio_FragmentCarteleras.
     */
    // TODO: Rename and change types and number of parameters
    public static inicio_FragmentCarteleras newInstance(String param1, String param2) {
        inicio_FragmentCarteleras fragment = new inicio_FragmentCarteleras();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio__carteleras, container, false);

        lvCarteleras = view.findViewById(R.id.lvCarteleras);
        cargarCartelerasListView();

        return view;
    }

    private void cargarCartelerasListView(){

        String jsonStr = "[{\"id\": \"1\",\"nombre\":\"UFC 313\",\"lugar\": \"T-Mobile Arena, Las Vegas United States\",\"dia\": \"Dom, Mar 9\",\"peleas\":[{\"id\": \"10\",\"peleador1\":{\"id\": \"100\",\"nombre\": \"Alex Pereira\",\"nacionalidad\": \"Brasil\",\"division\": \"LightHeavyWeight\"},\"peleador2\":{\"id\": \"101\",\"nombre\": \"Magomed Ankalaev\",\"nacionalidad\": \"Rusia\",\"division\": \"LightHeavyWeight\"},\"vencedor\": \"101\",\"metodo_victoria\": \"UD\"},{\"id\": \"11\",\"peleador1\":{\"id\": \"102\",\"nombre\": \"Justin Gaethje\",\"nacionalidad\": \"EEUU\",\"division\": \"LightWeight\"},\"peleador2\":{\"id\": \"103\",\"nombre\": \"Rafael Fiziev\",\"nacionalidad\": \"Azerbaiyan\",\"division\": \"LightWeight\"},\"vencedor\": \"102\",\"metodo_victoria\": \"UD\"},{\"id\": \"12\",\"peleador1\":{\"id\": \"104\",\"nombre\": \"Jalin Turner\",\"nacionalidad\": \"EEUU\",\"division\": \"LightWeight\"},\"peleador2\":{\"id\": \"105\",\"nombre\": \"Ignacio Bahamondes\",\"nacionalidad\": \"Chile\",\"division\": \"LightWeight\"},\"vencedor\": \"105\",\"metodo_victoria\": \"Submision\"}]}]";
        try {
            JSONArray jsonArrayCarteleras = new JSONArray(jsonStr);
            carteleras = new ArrayList<>();
            for (int i = 0; i < jsonArrayCarteleras.length(); i++) {
                //Recuperación Cartelera
                JSONObject objCartelera = jsonArrayCarteleras.getJSONObject(i);
                Cartelera cartelera = new Cartelera();
                cartelera.setIdCartelera(objCartelera.getInt("id"));
                cartelera.setNombreCartelera(objCartelera.getString("nombre"));
                cartelera.setLugar(objCartelera.getString("lugar"));
                cartelera.setFecha(objCartelera.getString("dia"));

                //Recuperación Peleas
                ArrayList<Pelea> peleas = new ArrayList<>();
                JSONArray jsonArrayPeleas = objCartelera.getJSONArray("peleas");
                for (int j = 0; j < jsonArrayPeleas.length(); j++){
                    JSONObject objPelea = jsonArrayPeleas.getJSONObject(j);
                    Pelea pelea = new Pelea();
                    pelea.setIdPelea(objPelea.getInt("id"));

                    //Peleador1
                    JSONObject objPeleador1 = objPelea.getJSONObject("peleador1");
                    Peleador peleador1 = new Peleador();
                    peleador1.setIdPeleador(objPeleador1.getInt("id"));
                    peleador1.setNombrePeleador(objPeleador1.getString("nombre"));
                    peleador1.setNacionalidad(objPeleador1.getString("nacionalidad"));
                    peleador1.setDivision(objPeleador1.getString("division"));
                    pelea.setPeleador1(peleador1);

                    //Peleador2
                    JSONObject objPeleador2 = objPelea.getJSONObject("peleador2");
                    Peleador peleador2 = new Peleador();
                    peleador2.setIdPeleador(objPeleador2.getInt("id"));
                    peleador2.setNombrePeleador(objPeleador2.getString("nombre"));
                    peleador2.setNacionalidad(objPeleador2.getString("nacionalidad"));
                    peleador2.setDivision(objPeleador2.getString("division"));
                    pelea.setPeleador2(peleador2);

                    //Recuperar al vencedor
                    int idVencedor = objPelea.getInt("vencedor");
                    if(idVencedor == peleador1.getIdPeleador()){
                        pelea.setVencedor(peleador1);
                    }else if(idVencedor == peleador2.getIdPeleador()){
                        pelea.setVencedor(peleador2);
                    }
                    pelea.setMetodo_victoria(objPelea.getString("metodo_victoria"));

                    peleas.add(pelea);
                }
                cartelera.setPeleas(peleas);
                carteleras.add(cartelera);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        AdaptadorPersonalizadoCarteleras adaptadorPersonalizadoCarteleras = new AdaptadorPersonalizadoCarteleras(requireContext(), carteleras);
        lvCarteleras.setAdapter(adaptadorPersonalizadoCarteleras);
    }

}