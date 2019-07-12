package senac.finance;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import senac.finance.models.Finance;

public class FinanceActivity extends AppCompatActivity {

    CalendarView dia;
    RadioButton tipo;
    EditText valor;
    Date diaSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dia = findViewById(R.id.calendarDia);
        tipo = findViewById(R.id.rbReceita);
        valor = findViewById(R.id.txtValor);

        if (MainActivity.alterar){
            try {
                dia.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(MainActivity.financeSelecionado.getDia()).getTime());
                tipo.setChecked(MainActivity.financeSelecionado.getTipo().equals("Receita"));
                valor.setText(String.valueOf(MainActivity.financeSelecionado.getValor()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        dia.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {
                diaSelecionado = new GregorianCalendar(i, i1, i2).getTime();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String diaFormat = new SimpleDateFormat("yyyy-MM-dd").format(diaSelecionado);

                    String tipoSelec = tipo.isChecked() ? "Receita" : "Despesa";

                    Double valorSelec = Double.parseDouble(valor.getText().toString());

                    if (MainActivity.alterar){
                        Finance finance = new Finance(MainActivity.financeSelecionado.getId(),
                                diaFormat, tipoSelec, valorSelec);

                        if (MainActivity.financeDB.update(finance)){
                            finish();
                        }
                    } else {
                        Finance finance = new Finance(0,
                                diaFormat, tipoSelec, valorSelec);

                        if (MainActivity.financeDB.insert(finance)){
                            finish();
                        }
                    }
                } catch (Exception ex){
                    Log.e("FinanceActivity", ex.getMessage());
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
