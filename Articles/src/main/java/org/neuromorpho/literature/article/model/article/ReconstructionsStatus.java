/*
 * Copyright (c) 2015-2022, Patricia Maraver
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


package org.neuromorpho.literature.article.model.article;

import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.time.LocalDate;
import java.util.*;

public class ReconstructionsStatus implements Comparable<ReconstructionsStatus> {

    public enum SpecificDetails {

        // Determining availability 
        TO_BE_REQUESTED("To be requested", "Determining availability"),
        POSITIVE_RESPONSE("Positive response", "Determining availability"),
        INVITED("Invited", "Determining availability"),
        REMINDER("Reminder", "Determining availability"),
        PESTERING("Pestering", "Determining availability"),
        ULTIMATUM("Ultimatum", "Determining availability"),
        // Not available
        BOUNCED("Bounced", "Not available"),
        BOUNCED_NEGATIVE("Bounced Negative", "Not available"),
        NOT_BOUNCED("Not Bounced", "Not available"),

        NO_CONTACT_INFORMATION("No contact information", "Not available"),
        NO_RESPONSE("No response", "Not available"),
        REQUEST_DECLINED("Request declined", "Not available"),
        LOST_DATA("Lost data", "Not available"),
        COMMUNICATION_STOPPED("Communication stopped", "Not available"),

        //Available
        IN_PROCESSING_PIPELINE("In processing pipeline", "Available"),
        IN_RELEASE("In release", "Available"),
        IN_REPOSITORY("In repository", "Available"),
        ON_HOLD("On hold", "Available");

        private final String details;
        private final String status;

        private SpecificDetails(String d, String s) {
            details = d;
            status = s;
        }

        public static SpecificDetails getSpecificDetails(String value) {
            for (SpecificDetails v : values()) {
                if (v.getDetails().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }

        public String getDetails() {
            return this.details;
        }

        public static String getGlobalStatus(List<SpecificDetails> statusList) {
            if (statusList.contains(IN_PROCESSING_PIPELINE)
                    || statusList.contains(IN_RELEASE)
                    || statusList.contains(ON_HOLD)
                    || statusList.contains(IN_REPOSITORY)) {
                return "Available";
            }
            if (statusList.contains(TO_BE_REQUESTED)
                    || statusList.contains(POSITIVE_RESPONSE)
                    || statusList.contains(INVITED)
                    || statusList.contains(REMINDER)
                    || statusList.contains(PESTERING)
                    || statusList.contains(ULTIMATUM)) {
                return "Determining availability";
            } else {
                return "Not available";
            }

        }

        public SpecificDetails getNext() {
            if (details.equals(SpecificDetails.TO_BE_REQUESTED.details)) {
                return SpecificDetails.INVITED;
            } else if (details.equals(SpecificDetails.INVITED.details)) {
                return SpecificDetails.REMINDER;
            } else if (details.equals(SpecificDetails.REMINDER.details)) {
                return SpecificDetails.PESTERING;
            } else if (details.equals(SpecificDetails.PESTERING.details)) {
                return SpecificDetails.ULTIMATUM;
            } else if (details.equals(SpecificDetails.BOUNCED.details) ||
                    details.equals(SpecificDetails.BOUNCED_NEGATIVE.details)) {
                return SpecificDetails.TO_BE_REQUESTED;
            }
            return null;
        }

        public String getStatus() {
            return this.status;
        }

        public static List<String> getStatus2DetailsStr(String status) {
            List<String> detailList = new ArrayList();
            for (SpecificDetails v : values()) {
                if (v.getStatus().equalsIgnoreCase(status)) {
                    detailList.add(v.toString());
                }
            }
            return detailList;
        }

        public static List<SpecificDetails> getStatus2Details(String status) {
            List<SpecificDetails> detailList = new ArrayList();
            for (SpecificDetails v : values()) {
                if (v.getStatus().equalsIgnoreCase(status)) {
                    detailList.add(v);
                }
            }
            return detailList;
        }

        public static List<String> getStatusList() {
            Set<String> detailSet = new HashSet();
            List<String> detailList = new ArrayList();
            for (SpecificDetails v : values()) {
                detailSet.add(v.getStatus());
            }
            detailList.addAll(detailSet);
            return detailList;
        }

        public static List<String> getDetailsList() {
            List<String> detailList = new ArrayList();
            for (SpecificDetails v : values()) {
                detailList.add(v.getDetails());
            }
            return detailList;
        }
    }

    private Integer index;
    private SpecificDetails specificDetails;
    private LocalDate date;
    private LocalDate expirationDate;
    private Double nReconstructions;

    public ReconstructionsStatus() {
    }

    public ReconstructionsStatus(ReconstructionsStatus reconstructionsStatus) {
        this.index = reconstructionsStatus.index;
        this.specificDetails = reconstructionsStatus.specificDetails;
        this.date = reconstructionsStatus.date;
        this.expirationDate = reconstructionsStatus.expirationDate;
        this.nReconstructions = reconstructionsStatus.nReconstructions;
    }

    public ReconstructionsStatus(Double nReconstructions, SpecificDetails specificDetails) {
        this.nReconstructions = nReconstructions;
        this.date = LocalDate.now();
        this.specificDetails = specificDetails;
        this.expirationDate = LocalDate.now();
        this.index = 1;
    }

    public ReconstructionsStatus(String specificDetails, LocalDate expirationDate, Double nReconstructions) {
        this.date = LocalDate.now();
        this.specificDetails = SpecificDetails.getSpecificDetails(specificDetails);
        this.expirationDate = expirationDate;
        if (nReconstructions == null) {
            this.nReconstructions = 1D;
        } else {
            this.nReconstructions = nReconstructions;
        }
        if (this.expirationDate == null) {
            switch (this.specificDetails) {
                case POSITIVE_RESPONSE:
                    this.expirationDate = LocalDate.now().plusDays(14);
                    break;
                case TO_BE_REQUESTED:
                    this.expirationDate = LocalDate.now();
                    break;
                case INVITED:
                case REMINDER:
                case PESTERING:
                case ULTIMATUM:
                    this.expirationDate = LocalDate.now().plusDays(7);
                    break;
                default:
                    this.expirationDate = null;
                    break;
            }

        }

    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public SpecificDetails getSpecificDetails() {
        return specificDetails;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setnReconstructions(Double nReconstructions) {
        this.nReconstructions = nReconstructions;
    }

    @Override
    public int compareTo(ReconstructionsStatus o) {
        return getDate().compareTo(o.getDate());
    }

    public void setSpecificDetails(SpecificDetails specificDetails) {
        this.specificDetails = specificDetails;
    }

    public Double getNReconstructions() {
        return nReconstructions;
    }

    public void setNReconstructions(Double nReconstructions) {
        this.nReconstructions = nReconstructions;
    }

    public Boolean is2BeRequested() {
        return this.specificDetails.equals(SpecificDetails.TO_BE_REQUESTED);
    }

    public Boolean isInitialized() {
        return this.specificDetails != null;
    }
    

    /*@Override
   /* public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReconstructionsStatus other = (ReconstructionsStatus) obj;

        if (!Objects.equals(this.index, other.index)) {
            return false;
        }
        if (this.specificDetails != other.specificDetails) {
            return false;
        }
        if (!isSameDay(this.expirationDate, other.expirationDate)) {
            return false;
        }
        if (!Objects.equals(this.nReconstructions, other.nReconstructions)) {
            return false;
        }
        return true;
    }

    private Boolean isSameDay(Date date1, Date date2) {
        Boolean sameDay = Boolean.FALSE;
        if (date1 != null && date2 != null) {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date1);
            cal2.setTime(date2);
            sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                    && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        } else if ((date1 == null && date2 == null)) {
            sameDay = Boolean.TRUE;
        }
        return sameDay;
    }*/

    @BsonIgnore
    public void next() {
        this.specificDetails = this.specificDetails.getNext();
        this.date = LocalDate.now();
        this.expirationDate = this.calculateExpirationDate();
    }

    @BsonIgnore
    private LocalDate calculateExpirationDate() {
        switch (this.specificDetails) {
            case TO_BE_REQUESTED:
                return LocalDate.now();
            case INVITED:
            case REMINDER:
            case PESTERING:
                return LocalDate.now().plusDays(7);
            case ULTIMATUM:
                return LocalDate.now().plusDays(14);
            default:
                break;
        }
        return null;
    }

    @Override
    public String toString() {
        return "ReconstructionsStatus{" +
                "index=" + index +
                ", specificDetails=" + specificDetails +
                ", date=" + date +
                ", expirationDate=" + expirationDate +
                ", nReconstructions=" + nReconstructions +
                '}';
    }
}
