package com.winksys.renaserv.web.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GenerateListaEventoExcel {
	
	private JSONArray data;

	public GenerateListaEventoExcel(JSONArray data) {
		this.data = data;
	}

	public File generate() {
		HSSFWorkbook wb = new HSSFWorkbook();
		
		HSSFCellStyle cellStyle = wb.createCellStyle();
		HSSFDataFormat df = wb.createDataFormat();
		cellStyle.setDataFormat(df.getFormat("d/m/yy h:mm"));
		
		HSSFCellStyle headerCellStyle = wb.createCellStyle();
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerCellStyle.setFont(headerFont);
		
		HSSFSheet sheet = wb.createSheet("Eventos");
		
		
		HSSFRow header = sheet.createRow(0);
		HSSFCell cell0 = header.createCell(0);
		cell0.setCellStyle(headerCellStyle);
						
		HSSFCell cell1 = header.createCell(1);
		cell1.setCellStyle(headerCellStyle);
		
		HSSFCell cell2 = header.createCell(2);
		cell2.setCellStyle(headerCellStyle);
		
		HSSFCell cell3 = header.createCell(3);
		cell3.setCellStyle(headerCellStyle);
		
		HSSFCell cell4 = header.createCell(4);
		cell4.setCellStyle(headerCellStyle);
		
		HSSFCell cell5 = header.createCell(5);
		cell5.setCellStyle(headerCellStyle);
		
		HSSFCell cell6 = header.createCell(6);
		cell6.setCellStyle(headerCellStyle);
		
		HSSFCell cell7 = header.createCell(7);
		cell7.setCellStyle(headerCellStyle);
		
		HSSFCell cell8 = header.createCell(8);
		cell8.setCellStyle(headerCellStyle);
		
		HSSFCell cell9 = header.createCell(9);
		cell9.setCellStyle(headerCellStyle);
		
		HSSFCell cell10 = header.createCell(10);
		cell10.setCellStyle(headerCellStyle);
		
		HSSFCell cell11 = header.createCell(11);
		cell11.setCellStyle(headerCellStyle);

		cell0.setCellValue("Placa");
		cell1.setCellValue("Cliente");
		cell2.setCellValue("Data Dados");
		cell3.setCellValue("Evento");
		cell4.setCellValue("Status");
		cell5.setCellValue("Usuário Tratativa");
		cell6.setCellValue("Data Finalização");
		cell7.setCellValue("Últ.Tratativa");
		cell8.setCellValue("Dt.Cadastro");
		cell9.setCellValue("Tempo Resolução");
		cell10.setCellValue("Dt.Primeira Tratativa");
		cell11.setCellValue("T.Resposta");
		
		
		
		for(int i = 0; i < data.size(); i++) {
			JSONObject hjson = data.getJSONObject(i);
			JSONObject idJson = hjson.getJSONObject("id");
			JSONObject veiculoJson = hjson.getJSONObject("veiculo");
			JSONObject clienteJson = hjson.getJSONObject("cliente");
			JSONObject tratativaJson = hjson.getJSONObject("tratativa");
			
			String status = "";
			switch (hjson.getInt("status")) {
			case 0: 
				status = "Pendente";
				break;
			case 1:
				status = "Em andamento";
				break;
			case 2:
				status = "Finalizado";
				break;
			}
			
			HSSFRow row = sheet.createRow(i+1);
			row.createCell(0).setCellValue(veiculoJson.getString("placa"));
			row.createCell(1).setCellValue(clienteJson.getString("nome"));
			
			HSSFCell cellDataDados = row.createCell(2);
			cellDataDados.setCellStyle(cellStyle);
			cellDataDados.setCellValue(new Date(idJson.getLong("dataDados")));
			
			row.createCell(3).setCellValue(hjson.getString("evento"));
			row.createCell(4).setCellValue(status);
			
			if (tratativaJson.containsKey("usuario")) {
				row.createCell(5).setCellValue(tratativaJson.getString("usuario"));
			}
			if (tratativaJson.containsKey("data")) {
				HSSFCell cellTratativa = row.createCell(6);
				cellTratativa.setCellStyle(cellStyle);
				cellTratativa.setCellValue(new Date(tratativaJson.getLong("data")));
			}
			if (tratativaJson.containsKey("tratativa")) {
				HSSFCell cellTratativa = row.createCell(7);
				cellTratativa.setCellStyle(cellStyle);
				cellTratativa.setCellValue(tratativaJson.getString("tratativa"));
			}
			
			HSSFCell cellDataCadastro = row.createCell(8);
			cellDataCadastro.setCellValue(new Date(hjson.getLong("dataCad")));
			cellDataCadastro.setCellStyle(cellStyle);
			
			long tempo = tratativaJson.getLong("data") - hjson.getLong("dataCad");
			row.createCell(9).setCellValue(tempo / 3600000f);
			
			
			if (hjson.containsKey("dataPrimeiraTratativa")) {
				HSSFCell cellDataPrimeiraTrat = row.createCell(10);
				cellDataPrimeiraTrat.setCellValue(new Date(hjson.getLong("dataPrimeiraTratativa")));
				cellDataPrimeiraTrat.setCellStyle(cellStyle);

				tempo = hjson.getLong("dataPrimeiraTratativa") - hjson.getLong("dataCad");
				row.createCell(11).setCellValue(tempo / 3600000f);
			}
			
			
		}
		
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(6);
		sheet.autoSizeColumn(7);
		sheet.autoSizeColumn(8);
		sheet.autoSizeColumn(9);
		sheet.autoSizeColumn(10);
		sheet.autoSizeColumn(11);
		
		try {
			File file = File.createTempFile("historico_" + System.currentTimeMillis(), ".xls");
			FileOutputStream fos = new FileOutputStream(file);
			wb.write(fos);
			return file;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

}
