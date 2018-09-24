package group2.seshealthpatient;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import group2.seshealthpatient.Activities.MainActivity;

public class graphActivity extends AppCompatActivity {

    public void btnReturnClick(View view){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        BarChart barChart = (BarChart) findViewById(R.id.chart);

        barChart.animateY(2300);
        barChart.getLegend().setForm(Legend.LegendForm.CIRCLE);

        BarData data = new BarData(setData());
        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
    }
    private BarDataSet setData() {

        ArrayList<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(0f, 88f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 61f));
        entries.add(new BarEntry(3f, 70f));
        entries.add(new BarEntry(4f, 72f));
        entries.add(new BarEntry(5f, 66f));
        entries.add(new BarEntry(6f, 89f));

        BarDataSet set = new BarDataSet(entries, "Day");
        set.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set.setValueTextColor(Color.rgb(155,155,155));

        return set;
    }
}
