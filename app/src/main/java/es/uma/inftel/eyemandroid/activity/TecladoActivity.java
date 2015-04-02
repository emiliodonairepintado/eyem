package es.uma.inftel.eyemandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import es.uma.inftel.eyemandroid.R;


public class TecladoActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private EditText editText;
    private boolean mayuscula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teclado);

        initToolbar();

        String texto = getIntent().getStringExtra("texto");
        editText = (EditText) findViewById(R.id.editText);
        editText.setText(texto);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.abc_tab_indicator_material);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_teclado, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.btnGuardar:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("texto", editText.getText().toString());
        setResult(RESULT_OK, intent);
        super.finish();
    }

    private void anadirAlFinal(String letter) {
        Editable etext = editText.getText();
        editText.setText(editText.getText() + ((mayuscula) ? letter.toUpperCase() : letter.toLowerCase()));
        int position = etext.length();
        editText.setSelection(position+1);
    }



    public void clickA(View view) {anadirAlFinal("A");}

    public void clickB(View view) {anadirAlFinal("B");}

    public void clickC(View view) {anadirAlFinal("C");}

    public void clickD(View view) {anadirAlFinal("D");}

    public void clickE(View view) {anadirAlFinal("E");}

    public void clickF(View view) {anadirAlFinal("F");}

    public void clickG(View view) {anadirAlFinal("G");}

    public void clickH(View view) {anadirAlFinal("H");}

    public void clickI(View view) {anadirAlFinal("I");}

    public void clickJ(View view) {anadirAlFinal("J");}

    public void clickK(View view) {anadirAlFinal("K");}

    public void clickL(View view) {anadirAlFinal("L");}

    public void clickM(View view) {anadirAlFinal("M");}

    public void clickN(View view) {anadirAlFinal("N");}

    public void clickO(View view) {anadirAlFinal("O");}

    public void clickP(View view) {anadirAlFinal("P");}

    public void clickQ(View view) {anadirAlFinal("Q");}

    public void clickR(View view) {anadirAlFinal("R");}

    public void clickS(View view) {anadirAlFinal("S");}

    public void clickT(View view) {anadirAlFinal("T");}

    public void clickU(View view) {anadirAlFinal("U");}

    public void clickV(View view) {anadirAlFinal("V");}

    public void clickW(View view) {anadirAlFinal("W");}

    public void clickX(View view) {anadirAlFinal("X");}

    public void clickY(View view) {anadirAlFinal("Y");}

    public void clickZ(View view) {anadirAlFinal("Z");}

    public void clickSp(View view) {anadirAlFinal(" ");}

    public void clickComa(View view) {anadirAlFinal(",");}

    public void clickPunto(View view) {anadirAlFinal(".");}

    public void clickPp(View view) {anadirAlFinal(":");}

    public void clickAroba(View view) {anadirAlFinal("@");}

    public void clickBara(View view) {anadirAlFinal("-");}

    public void clickBarra_(View view) {anadirAlFinal("_");}

    public void clickInte(View view) {anadirAlFinal("?");}

    public void clickInterroga(View view) {anadirAlFinal("¿");}

    public void clickBorrar(View view) {
        String text = editText.getText().toString();
        if (text.length() == 0) {
            return;
        }
        editText.setText(text.substring(0, text.length() - 1));
        Editable etext = editText.getText();
        int position = etext.length()-1;
        editText.setSelection(position+1);
    }

    public  void clickEnter(View view) {anadirAlFinal(""+'\n');}


    public void clickCero(View view) {anadirAlFinal("0");}

    public void clickUno(View view) {anadirAlFinal("1");}


    public void clickDos(View view) {anadirAlFinal("2");}


    public void click3(View view) {anadirAlFinal("3");}

    public void click4(View view) {anadirAlFinal("4");}

    public void click5(View view) {anadirAlFinal("5");}


    public void click6(View view) {anadirAlFinal("6");}

    public void click7(View view) {anadirAlFinal("7");}

    public void click8(View view) {anadirAlFinal("8");}

    public void click9(View view) {anadirAlFinal("9");}

    public void clickEx(View view) {anadirAlFinal("!");}

    public void clickMayuscula(View view) {
        mayuscula = !mayuscula;
        setLetra(R.id.button_a, "A", mayuscula);
        setLetra(R.id.button_b, "B", mayuscula);
        setLetra(R.id.button_c, "C", mayuscula);
        setLetra(R.id.button_d, "D", mayuscula);
        setLetra(R.id.button_e, "E", mayuscula);
        setLetra(R.id.button_f, "F", mayuscula);
        setLetra(R.id.button_g, "G", mayuscula);
        setLetra(R.id.button_h, "H", mayuscula);
        setLetra(R.id.button_i, "I", mayuscula);
        setLetra(R.id.button_j, "G", mayuscula);
        setLetra(R.id.button_k, "K", mayuscula);
        setLetra(R.id.button_l, "L", mayuscula);
        setLetra(R.id.button_m, "M", mayuscula);
        setLetra(R.id.button_n, "N", mayuscula);
        setLetra(R.id.button_o, "O", mayuscula);
        setLetra(R.id.button_p, "P", mayuscula);
        setLetra(R.id.button_q, "Q", mayuscula);
        setLetra(R.id.button_r, "R", mayuscula);
        setLetra(R.id.button_s, "S", mayuscula);
        setLetra(R.id.button_t, "T", mayuscula);
        setLetra(R.id.button_u, "U", mayuscula);
        setLetra(R.id.button_v, "V", mayuscula);
        setLetra(R.id.button_w, "W", mayuscula);
        setLetra(R.id.button_X, "X", mayuscula);
        setLetra(R.id.button_y, "Y", mayuscula);
        setLetra(R.id.button_z, "Z", mayuscula);
    }

    private void setLetra(int buttonId, String letter, boolean mayuscula) {
        Button button = (Button)findViewById(buttonId);
        button.setText((mayuscula) ? letter.toUpperCase() : letter.toLowerCase());
    }
}
