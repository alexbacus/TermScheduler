package com.example.alexbacus_termscheduler.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "term_table")
public class TermEntity {

        @PrimaryKey()
        private int termID;

        private String termName;
        private String startDate;
        private String endDate;

        public TermEntity(int termID, String termName, String startDate, String endDate) {
            this.termID = termID;
            this.termName = termName;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public int getTermID() {
            return termID;
        }

        public void setTermID(int termID) {
            this.termID = termID;
        }

        public String getTermName() {
            return termName;
        }

        public void setTermName(String termName) {
            this.termName = termName;
        }

        public String getStartDate() { return startDate; }

        public void setStartDate(String startDate) { this.startDate = startDate; }

        public String getEndDate() { return endDate; }

        public void setEndDate(String endDate) { this.endDate = endDate; }

        public String toString() {
            return termName;
        }
}
