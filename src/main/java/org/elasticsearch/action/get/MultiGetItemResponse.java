/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.action.get;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Streamable;

import java.io.IOException;

/**
 * A single multi get response.
 */
public class MultiGetItemResponse implements Streamable {

    private GetResponse response;
    private MultiGetResponse.Failure failure;

    MultiGetItemResponse() {

    }

    public MultiGetItemResponse(GetResponse response, MultiGetResponse.Failure failure) {
        this.response = response;
        this.failure = failure;
    }

    /**
     * The index name of the document.
     */
    public String index() {
        if (failure != null) {
            return failure.index();
        }
        return response.index();
    }

    /**
     * The index name of the document.
     */
    public String getIndex() {
        return index();
    }

    /**
     * The type of the document.
     */
    public String type() {
        if (failure != null) {
            return failure.type();
        }
        return response.type();
    }

    /**
     * The type of the document.
     */
    public String getType() {
        return type();
    }

    /**
     * The id of the document.
     */
    public String id() {
        if (failure != null) {
            return failure.id();
        }
        return response.id();
    }

    /**
     * The id of the document.
     */
    public String getId() {
        return id();
    }

    /**
     * Is this a failed execution?
     */
    public boolean failed() {
        return failure != null;
    }

    /**
     * Is this a failed execution?
     */
    public boolean isFailed() {
        return failed();
    }

    /**
     * The actual get response, <tt>null</tt> if its a failure.
     */
    public GetResponse response() {
        return this.response;
    }

    /**
     * The actual get response, <tt>null</tt> if its a failure.
     */
    public GetResponse getResponse() {
        return this.response;
    }

    /**
     * The failure if relevant.
     */
    public MultiGetResponse.Failure failure() {
        return this.failure;
    }

    /**
     * The failure if relevant.
     */
    public MultiGetResponse.Failure getFailure() {
        return failure();
    }

    public static MultiGetItemResponse readItemResponse(StreamInput in) throws IOException {
        MultiGetItemResponse response = new MultiGetItemResponse();
        response.readFrom(in);
        return response;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        if (in.readBoolean()) {
            failure = MultiGetResponse.Failure.readFailure(in);
        } else {
            response = new GetResponse();
            response.readFrom(in);
        }
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        if (failure != null) {
            out.writeBoolean(true);
            failure.writeTo(out);
        } else {
            out.writeBoolean(false);
            response.writeTo(out);
        }
    }
}