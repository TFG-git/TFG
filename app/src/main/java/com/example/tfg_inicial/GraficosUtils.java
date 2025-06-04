package com.example.tfg_inicial;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GraficosUtils {

    // Gráfico de Barras para DetallePeleaFragment
    public static void mostrarBarChartEnfrentado(Context context, BarChart barChart, String[] labels, String[] valoresRojo, String[] valoresAzul) {
        // Parsear los strings a float, usando 0f si hay error
        float[] rojo = new float[labels.length];
        float[] azul = new float[labels.length];

        for (int i = 0; i < labels.length; i++) {
            try { rojo[i] = Float.parseFloat(valoresRojo[i] == null ? "0" : valoresRojo[i]); }
            catch (Exception e) { rojo[i] = 0f; }

            try { azul[i] = Float.parseFloat(valoresAzul[i] == null ? "0" : valoresAzul[i]); }
            catch (Exception e) { azul[i] = 0f; }
        }

        List<BarEntry> entradasRojo = new ArrayList<>();
        List<BarEntry> entradasAzul = new ArrayList<>();
        for (int i = 0; i < labels.length; i++) {
            entradasRojo.add(new BarEntry(i, rojo[i]));
            entradasAzul.add(new BarEntry(i, azul[i]));
        }

        BarDataSet dataSetRojo = new BarDataSet(entradasRojo, "Rojo");
        dataSetRojo.setColor(ContextCompat.getColor(context, R.color.rojo2));
        dataSetRojo.setValueTextColor(ContextCompat.getColor(context, R.color.blanco));
        dataSetRojo.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                return ((int) barEntry.getY()) + "%";
            }
        });

        BarDataSet dataSetAzul = new BarDataSet(entradasAzul, "Azul");
        dataSetAzul.setColor(ContextCompat.getColor(context, R.color.azul));
        dataSetAzul.setValueTextColor(ContextCompat.getColor(context, R.color.blanco));
        dataSetAzul.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                return ((int) barEntry.getY()) + "%";
            }
        });

        BarData data = new BarData(dataSetRojo, dataSetAzul);

        float groupSpace = 0.25f;
        float barSpace = 0.03f;
        float barWidth = 0.35f;
        data.setBarWidth(barWidth);

        barChart.setData(data);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * labels.length);

        // Agrupar las barras
        data.groupBars(0, groupSpace, barSpace);

        // XAxis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(ContextCompat.getColor(context, R.color.blanco));
        xAxis.setCenterAxisLabels(true); // CRUCIAL para agrupadas

        // YAxis
        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setTextColor(ContextCompat.getColor(context, R.color.blanco));
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);

        // Quitar descripción y leyenda
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        data.setValueTextSize(14f);

        // Limpieza visual
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);

        // Animación
        barChart.animateY(900);

        // Refrescar
        barChart.invalidate();
    }

    // Gráfico para Perfil de Peleador (Dialog)
    public static void mostrarPieChartPorcentaje(Context context, PieChart pieChart, float porcentaje, String labelPositivo, String labelNegativo, int colorPositivo, int colorNegativo) {
        List<PieEntry> entries = new ArrayList<>();
        float porcentajeLimpiado = Math.max(0, Math.min(100, porcentaje));
        float negativo = 100f - porcentajeLimpiado;

        entries.add(new PieEntry(porcentajeLimpiado, labelPositivo));
        entries.add(new PieEntry(negativo, labelNegativo));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colorPositivo, colorNegativo);
        dataSet.setValueTextSize(16f);
        dataSet.setValueTextColor(ContextCompat.getColor(context, R.color.blanco));
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.getDefault(), "%.0f%%", value);
            }
        });

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(true);
        pieChart.getLegend().setTextColor(ContextCompat.getColor(context, R.color.blanco));

        pieChart.animateY(700);
        pieChart.invalidate();
    }

    public static int parseStat(String stat) {
        try {
            return stat == null ? 0 : Integer.parseInt(stat);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    public static String[] calcularPorcentajes(String[] valores) {
        float total = 0f;
        float[] flotantes = new float[valores.length];
        String[] porcentajes = new String[valores.length];

        // Parsear y sumar el total
        for (int i = 0; i < valores.length; i++) {
            try {
                flotantes[i] = Float.parseFloat(valores[i] == null ? "0" : valores[i]);
            } catch (Exception e) {
                flotantes[i] = 0f;
            }
            total += flotantes[i];
        }

        // Calcular el porcentaje de cada valor
        for (int i = 0; i < valores.length; i++) {
            if (total > 0) {
                porcentajes[i] = String.valueOf(Math.round((flotantes[i] / total) * 100));
            } else {
                porcentajes[i] = "0";
            }
        }
        return porcentajes;
    }
}
