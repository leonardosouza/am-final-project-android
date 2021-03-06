package br.com.notifycar.repository.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.notifycar.controller.CadastroVeiculoActivity;
import br.com.notifycar.helper.CamposHelper;
import br.com.notifycar.util.UtilJson;

/**
 * Created by Desenvolvimento on 23/09/2016.
 */
public class CadastroUsuarioTask extends AsyncTask<String, Void, String> {

    private Activity activity;
    private ProgressDialog progressDialog;
    private String json = "";
    CamposHelper helper = new CamposHelper();

    public CadastroUsuarioTask(Activity activity){
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Carregando...");
        progressDialog.setMessage("Aguarde");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        try{
            URL url = new URL("http://notifycar-api.mybluemix.net/usuario");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());


            out.writeBytes(helper.recuperaCamposUsuario(activity).toString());

            out.flush();
            out.close();


            InputStream conteudo = conn.getInputStream();
            json = UtilJson.toString(conteudo);

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return json;
    }

    @Override
    protected void onPostExecute(String json) {
      String id = "";
        try {
            JSONObject usuario = new JSONObject(json);
            id = usuario.getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Toast.makeText(activity, "Usuario cadastrado com Sucesso! "+id, Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        Intent it = new Intent(activity, CadastroVeiculoActivity.class);
        it.putExtra("idUsuario", id);
        activity.startActivity(it);
    }
}
