/*
 * Copyright (c) 2015-2021, Patricia Maraver
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *  
 */


package org.neuromorpho.literature.api.service;

import org.neuromorpho.literature.api.exceptions.UnknownTypeException;
import org.neuromorpho.literature.api.model.Version;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.opc.internal.FileHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.*;
import org.neuromorpho.literature.api.service.dto.ArticleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 *  Excel Reports Service. Spring MVC Pattern
 */
@Service
public class ReportService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Value("${folder}")
    private String folder;

    @Autowired
    private LiteratureService literatureService;
    @Autowired
    private VersionService versionService;

    public String generateReport(String type) throws Exception {

        String fileName = "";
        switch (type) {
            case "CompleteDetails": {
                fileName = type + ".xlsx";
                XSSFWorkbook workbook = this.generateCompleteDetails();
                // Write the output to a file
                FileOutputStream fileOut;
                try {
                    File dir = new File(folder);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    fileOut = new FileOutputStream(folder + fileName);
                    workbook.write(fileOut);
                    fileOut.close();
                    workbook.close();

                } catch (Exception ex) {
                    log.error("Exception generating report " + type + ": ", ex);
                }
                break;
            }
            case "FrozenEvolution": {
                try {
                    //create a copy of last month
                    fileName = type + ".xlsx";
                    Version version = versionService.getVersion("literature");

                    File bkFile = new File(folder + type + ".xlsx");
                    File lastMonthFile = new File(folder + type
                            + "_" + version.getVersion() + ".xlsx");
                    FileHelper.copyFile(bkFile, lastMonthFile);
                    XSSFWorkbook workbook = this.updateFrozenEvolution(type, version);
                    FileOutputStream fileOut;

                    File dir = new File(folder);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    fileOut = new FileOutputStream(bkFile, false);
                    workbook.write(fileOut);
                    fileOut.close();
                    workbook.close();
                } catch (Exception ex) {
                    System.out.println("Exception generating report " + type + ": " + ex);
                    log.error("Exception generating report " + type + ": ", ex);
                }
                break;
            }

            default:
                throw new UnknownTypeException("Report type" + type);
        }
        return folder + fileName;
    }

    private XSSFWorkbook generateCompleteDetails() throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("complete-details");
        Integer rowIndex = 0;
        Integer columnIndex = 0;
        XSSFRow row = sheet.createRow(rowIndex++);

        XSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_80_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = workbook.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        style.setFont(font);

        row.createCell(columnIndex++).setCellValue("PMID");
        row.createCell(columnIndex++).setCellValue("DOI");
        row.createCell(columnIndex++).setCellValue("Year");
        row.createCell(columnIndex++).setCellValue("Journal");
        row.createCell(columnIndex++).setCellValue("Paper_Title");
        row.createCell(columnIndex++).setCellValue("First_Author");
        row.createCell(columnIndex++).setCellValue("Last_Author");

        row.createCell(columnIndex++).setCellValue("Species");
        row.createCell(columnIndex++).setCellValue("BrainRegion");
        row.createCell(columnIndex++).setCellValue("CellType");
        row.createCell(columnIndex++).setCellValue("NoReconstructions");
        row.createCell(columnIndex++).setCellValue("SystemUsed");
        row.createCell(columnIndex++).setCellValue("OCDate");
        row.createCell(columnIndex++).setCellValue("SpecificDetails");
        row.createCell(columnIndex++).setCellValue("Status");
        row.createCell(columnIndex++).setCellValue("Usage");

        for (int j = 0; j <= 15; j++) {
            row.getCell(j).setCellStyle(style);
        }

        Integer page = 0;
        HashMap<String, String> query = new HashMap();
        query.put("data.dataUsage", "DESCRIBING_NEURONS");
        Page<ArticleDto> articlePage = literatureService.findPublicationsByQuery(page, query);
        log.debug("Total elements: " + articlePage.getTotalElements());
        Integer count = 0;
        while (page < articlePage.getTotalPages()) {

            for (ArticleDto article : articlePage.getContent()) {
                try {
                    if (article.getSpecificDetails() != null && !article.getSpecificDetails().isEmpty()) {
                        columnIndex = 0;
                        // Create a row and put some cells in it. Rows are 0 based.
                        row = sheet.createRow(rowIndex++);
                        if (article.getPmid() != null) {
                            row.createCell(columnIndex++).setCellValue(article.getPmid());
                        } else {
                            row.createCell(columnIndex++);
                        }
                        if (article.getDoi() != null) {
                            row.createCell(columnIndex++).setCellValue(article.getDoi());
                        } else {
                            row.createCell(columnIndex++);
                        }
                        if (article.getPublishedDate() != null) {
                            row.createCell(columnIndex++).setCellValue(new SimpleDateFormat("yyyy").format(article.getPublishedDate()));
                        } else {
                            row.createCell(columnIndex++);
                        }
                        if (article.getJournal() != null) {
                            row.createCell(columnIndex++).setCellValue(article.getJournal());
                        } else {
                            row.createCell(columnIndex++);
                        }
                        if (article.getTitle() != null) {
                            row.createCell(columnIndex++).setCellValue(article.getTitle());
                        } else {
                            row.createCell(columnIndex++);
                        }
                        if (article.getAuthorList() != null && article.getAuthorList().size() > 0) {
                            String firstAuthor = article.getAuthorList().get(0);
                            if (firstAuthor == null) {
                                row.createCell(columnIndex++);
                            } else {
                                row.createCell(columnIndex++).setCellValue(firstAuthor);
                            }
                            if (article.getAuthorList().size() - 1 != 0) {
                                String lastAuthor = article.getAuthorList().get(article.getAuthorList().size() - 1);
                                row.createCell(columnIndex++).setCellValue(lastAuthor);
                            } else {
                                row.createCell(columnIndex++);
                            }
                        } else {
                            row.createCell(columnIndex++);
                        }
                        if (article.getMetadata().get("species") == null) {
                            row.createCell(columnIndex++);
                        } else {
                            row.createCell(columnIndex++).setCellValue(article.getMetadata().get("species").toString());
                        }
                        if (article.getMetadata().get("brainRegion") == null) {
                            row.createCell(columnIndex++);
                        } else {
                            row.createCell(columnIndex++).setCellValue(article.getMetadata().get("brainRegion").toString());
                        }
                        if (article.getMetadata().get("cellType") == null) {
                            row.createCell(columnIndex++);
                        } else {
                            row.createCell(columnIndex++).setCellValue(article.getMetadata().get("cellType").toString());
                        }
                        if (article.getnReconstructions() == null) {
                            row.createCell(columnIndex++).setCellValue(0);
                        } else {
                            row.createCell(columnIndex++).setCellValue(article.getnReconstructions());
                        }
                        if (article.getMetadata().get("tracingSystem") == null) {
                            row.createCell(columnIndex++);
                        } else {
                            row.createCell(columnIndex++).setCellValue(article.getMetadata().get("tracingSystem").toString());
                        }

                        if (article.getEvaluatedDate() != null) {
                            row.createCell(columnIndex++).setCellValue(new SimpleDateFormat("MM/dd/yyyy").format(article.getEvaluatedDate()));
                        } else {
                            row.createCell(columnIndex++);

                        }

                        row.createCell(columnIndex++).setCellValue(article.getSpecificDetails());
                        row.createCell(columnIndex++).setCellValue(article.getGlobalStatus());
                        row.createCell(columnIndex++).setCellValue(article.getDataUsage().toString());

                        style = this.fillColor(workbook, article.getSpecificDetails());
                    }
                } catch (Exception ex) {
                    log.error("Exception in article: ", article.getTitle());
                    log.error("Exception creating report: ", ex);
                }

                for (int j = 0; j <= 15; j++) {
                    try {
                        row.getCell(j).setCellStyle(style);
                    } catch (Exception ex) {
                        log.error("Exception giving style to cell: ", article.getId());
                    }
                }
                count++;
                
            }

            page++;
            articlePage = literatureService.findPublicationsByQuery(page, query);

        }
        log.debug("Total articles: " + count);
        return workbook;
    }

    private XSSFWorkbook updateFrozenEvolution(String fileName, Version version) {
        InputStream myxls = null;
        XSSFWorkbook workbook = null;
        try {

            Map<String, Object> articlesSummary = literatureService.getSummary();
            System.out.println("ArticlesSummary: " + articlesSummary);

            myxls = new FileInputStream(folder + fileName + ".xlsx");
            workbook = new XSSFWorkbook(myxls);
            XSSFSheet sheet = workbook.getSheet("frozen-evolution");
            Integer rowIndex = sheet.getPhysicalNumberOfRows();
            Integer columnIndex = 0;
            XSSFRow previousRow = sheet.getRow(rowIndex - 1);
            String oldVersion = previousRow.getCell(columnIndex).getRichStringCellValue().getString();
            XSSFRow row;
            log.debug("Generating FrozenEvolution; Old version: " + oldVersion + " New version: " + version.getVersion());
            if (!oldVersion.equals(version.getVersion())) {
                row = sheet.createRow(rowIndex);
            } else {
                row = previousRow;
                previousRow = sheet.getRow(rowIndex - 2);
            }

            //colors
            XSSFCellStyle orangeStyle = workbook.createCellStyle();
            orangeStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.ORANGE.getIndex());
            orangeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            XSSFCellStyle yellowStyle = workbook.createCellStyle();
            yellowStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
            yellowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            //Version
            row.createCell(columnIndex++).setCellValue(version.getVersion());

            //Date
            row.createCell(columnIndex++).setCellValue(getDateYear(version.getDate()));
            row.createCell(columnIndex++).setCellValue(getPreviousMonth(version.getDate()));
            row.createCell(columnIndex++).setCellValue(getDateMonth(version.getDate()));

            //mined total
            Double varTotalArticles = previousRow.getCell(columnIndex).getNumericCellValue();

            log.debug("Available: " + articlesSummary.get("Available"));
            log.debug("Not available: " + articlesSummary.get("Not available"));
            log.debug("Determining availability: " + articlesSummary.get("Determining availability"));

            Integer describing = (Integer) articlesSummary.get("Available") +
                    (Integer) articlesSummary.get("Not available") +
                    (Integer) articlesSummary.get("Determining availability");
            log.debug("Describing: " + describing);

            Long totalArticles = (Long) articlesSummary.get("Negatives") + describing;
            row.createCell(columnIndex++).setCellValue(totalArticles);
            row.createCell(columnIndex++).setCellValue(totalArticles - varTotalArticles.longValue()); //substract previous column

            log.debug("Total: " + totalArticles);

            //Positives describing_neurons
            Double totalReconstructions = (Double) articlesSummary.get("Available.nReconstructions")
                    + (Double) articlesSummary.get("Not available.nReconstructions")
                    + (Double) articlesSummary.get("Determining availability.nReconstructions");

            Double varTotalReconstructions = previousRow.getCell(columnIndex).getNumericCellValue();
            row.createCell(columnIndex++).setCellValue(totalReconstructions);
            XSSFCell cell = row.createCell(columnIndex++);
            cell.setCellValue(totalReconstructions - varTotalReconstructions); //substract previous column
            cell.setCellStyle(orangeStyle);
            Double varTotalPublications = previousRow.getCell(columnIndex).getNumericCellValue();
            cell = row.createCell(columnIndex++);
           
            cell.setCellValue(describing);
            cell.setCellStyle(yellowStyle);
            cell = row.createCell(columnIndex++);
            cell.setCellValue(describing - varTotalPublications); //substract previous column
            cell.setCellStyle(orangeStyle);

            //Available
            Double varAvailablePublications = previousRow.getCell(columnIndex).getNumericCellValue();
            cell = row.createCell(columnIndex++);
            cell.setCellValue((Integer) articlesSummary.get("Available"));
            cell.setCellStyle(yellowStyle);
            cell = row.createCell(columnIndex++);
            cell.setCellValue((Integer) articlesSummary.get("Available") - varAvailablePublications); //substract previous column
            cell.setCellStyle(orangeStyle);

            Double varAvailableReconstructions = previousRow.getCell(columnIndex).getNumericCellValue();
            cell = row.createCell(columnIndex++);
            cell.setCellValue((Double) articlesSummary.get("Available.nReconstructions"));
            cell.setCellStyle(yellowStyle);

            cell = row.createCell(columnIndex++);
            cell.setCellValue((Double) articlesSummary.get("Available.nReconstructions") - varAvailableReconstructions); //substract previous column
            cell.setCellStyle(orangeStyle);

            //Not available
            Double varNotAvailablePublications = previousRow.getCell(columnIndex).getNumericCellValue();
            cell = row.createCell(columnIndex++);
            cell.setCellValue((Integer) articlesSummary.get("Not available"));
            cell.setCellStyle(yellowStyle);

            cell = row.createCell(columnIndex++);
            cell.setCellValue((Integer) articlesSummary.get("Not available") - varNotAvailablePublications); //substract previous column
            cell.setCellStyle(orangeStyle);

            Double varNotAvailableReconstructions = previousRow.getCell(columnIndex).getNumericCellValue();
            cell = row.createCell(columnIndex++);
            cell.setCellValue((Double) articlesSummary.get("Not available.nReconstructions"));
            cell.setCellStyle(yellowStyle);

            cell = row.createCell(columnIndex++);
            cell.setCellValue((Double) articlesSummary.get("Not available.nReconstructions") - varNotAvailableReconstructions); //substract previous column
            cell.setCellStyle(orangeStyle);

            //Determining availability
            Double varDeterminingAvailabilityPublications = previousRow.getCell(columnIndex).getNumericCellValue();
            cell = row.createCell(columnIndex++);
            cell.setCellValue((Integer) articlesSummary.get("Determining availability"));
            cell.setCellStyle(yellowStyle);

            cell = row.createCell(columnIndex++);
            cell.setCellValue((Integer) articlesSummary.get("Determining availability") - varDeterminingAvailabilityPublications); //substract previous column
            cell.setCellStyle(orangeStyle);

            Double varDeterminingAvailabilityReconstructions = previousRow.getCell(columnIndex).getNumericCellValue();
            cell = row.createCell(columnIndex++);
            cell.setCellValue((Double) articlesSummary.get("Determining availability.nReconstructions"));
            cell.setCellStyle(yellowStyle);

            cell = row.createCell(columnIndex++);
            cell.setCellValue((Double) articlesSummary.get("Determining availability.nReconstructions") - varDeterminingAvailabilityReconstructions); //substract previous column
            cell.setCellStyle(orangeStyle);

        } catch (Exception ex) {
            System.out.println("Exception reading report: " + ex);
            log.error("Exception reading report: ", ex);
        }
        return workbook;

    }

    private String getPreviousMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        DateFormat monthFormat = new SimpleDateFormat("MMM");
        return monthFormat.format(calendar.getTime());
    }

    private XSSFCellStyle fillColor(XSSFWorkbook workbook, String specificDetails) {
        XSSFCellStyle style = workbook.createCellStyle();
        Short color = HSSFColor.HSSFColorPredefined.WHITE.getIndex();
        if (specificDetails != null) {
            if (specificDetails.contains("No contact information") ||
                    specificDetails.contains("Request declined") ||
                    specificDetails.contains("Lost data") ||
                    specificDetails.contains("Communication Stopped") ||
                    specificDetails.contains("No response")) {
                color = HSSFColor.HSSFColorPredefined.ROSE.getIndex();
            } else if (specificDetails.contains("To be requested")) {
                color = HSSFColor.HSSFColorPredefined.YELLOW.getIndex();
            } else if (specificDetails.contains("Positive response")) {
                color = HSSFColor.HSSFColorPredefined.LIGHT_BLUE.getIndex();
            } else if (specificDetails.contains("In processing pipeline")) {
                color = HSSFColor.HSSFColorPredefined.TURQUOISE.getIndex();
            } else if (specificDetails.contains("Invited")) {
                color = HSSFColor.HSSFColorPredefined.LIGHT_CORNFLOWER_BLUE.getIndex();
            } else if (specificDetails.contains("In repository") ||
                    specificDetails.contains("In release")) {
                color = HSSFColor.HSSFColorPredefined.LIGHT_GREEN.getIndex();
            } else {
                color = HSSFColor.HSSFColorPredefined.WHITE.getIndex();
            }
        }


        style.setFillForegroundColor(color);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    protected String getDateMonth(Date date) {
        DateFormat yearFormat = new SimpleDateFormat("MMM");
        return yearFormat.format(date.getTime());
    }

    protected String getDateYear(Date date) {
        DateFormat yearFormat = new SimpleDateFormat("yyyy");
        return yearFormat.format(date.getTime());
    }
}
