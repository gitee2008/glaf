/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.glaf.matrix.data.mongo;

public enum ImageSizeEnum {
    PIXELS_200(200, "x2"),
    PIXELS_400(400, "x4"),
    PIXELS_600(600, "x6"),
    PIXELS_800(800, "x8"),
    PIXELS_MAX(1024, "xm");//最大1024
	
    public int size;
    public String path;

    ImageSizeEnum(int size, String path) {
        this.size = size;
        this.path = path;
    }

    public static ImageSizeEnum valueOfPath(String path) {
        if(path == null) {
            return  null;
        }
        for(ImageSizeEnum e : ImageSizeEnum.values()) {
            if(e.path.equalsIgnoreCase(path)) {
                return e;
            }
        }
        return null;
    }
}
