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

package org.neuromorpho.literature.search.model;

import java.time.LocalDateTime;


public class Log {
    
    private LocalDateTime start;
    private LocalDateTime stop;
    private Long threadId;
    private String cause;

    public Log() {
        this.start = LocalDateTime.now();
        this.cause = "Executing ...";
        this.stop = null;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getStop() {
        return stop;
    }

    public void setStop(LocalDateTime stop) {
        this.stop = stop;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }
    

    public String getCause() {
        return cause;
    }
    
    public void setCause(String cause) {
        this.cause = cause;
        this.stop = LocalDateTime.now();

    }

}
