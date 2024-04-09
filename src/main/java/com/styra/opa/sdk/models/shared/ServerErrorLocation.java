/* 
 * Code generated by Speakeasy (https://speakeasyapi.dev). DO NOT EDIT.
 */

package com.styra.opa.sdk.models.shared;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.styra.opa.sdk.utils.Utils;
import java.io.InputStream;
import java.lang.Deprecated;
import java.math.BigDecimal;
import java.math.BigInteger;


public class ServerErrorLocation {

    @JsonProperty("file")
    private String file;

    @JsonProperty("row")
    private long row;

    @JsonProperty("col")
    private long col;

    public ServerErrorLocation(
            @JsonProperty("file") String file,
            @JsonProperty("row") long row,
            @JsonProperty("col") long col) {
        Utils.checkNotNull(file, "file");
        Utils.checkNotNull(row, "row");
        Utils.checkNotNull(col, "col");
        this.file = file;
        this.row = row;
        this.col = col;
    }

    public String file() {
        return file;
    }

    public long row() {
        return row;
    }

    public long col() {
        return col;
    }

    public final static Builder builder() {
        return new Builder();
    }

    public ServerErrorLocation withFile(String file) {
        Utils.checkNotNull(file, "file");
        this.file = file;
        return this;
    }

    public ServerErrorLocation withRow(long row) {
        Utils.checkNotNull(row, "row");
        this.row = row;
        return this;
    }

    public ServerErrorLocation withCol(long col) {
        Utils.checkNotNull(col, "col");
        this.col = col;
        return this;
    }
    
    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServerErrorLocation other = (ServerErrorLocation) o;
        return 
            java.util.Objects.deepEquals(this.file, other.file) &&
            java.util.Objects.deepEquals(this.row, other.row) &&
            java.util.Objects.deepEquals(this.col, other.col);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(
            file,
            row,
            col);
    }
    
    @Override
    public String toString() {
        return Utils.toString(ServerErrorLocation.class,
                "file", file,
                "row", row,
                "col", col);
    }
    
    public final static class Builder {
 
        private String file;
 
        private Long row;
 
        private Long col;  
        
        private Builder() {
          // force use of static builder() method
        }

        public Builder file(String file) {
            Utils.checkNotNull(file, "file");
            this.file = file;
            return this;
        }

        public Builder row(long row) {
            Utils.checkNotNull(row, "row");
            this.row = row;
            return this;
        }

        public Builder col(long col) {
            Utils.checkNotNull(col, "col");
            this.col = col;
            return this;
        }
        
        public ServerErrorLocation build() {
            return new ServerErrorLocation(
                file,
                row,
                col);
        }
    }
}
