/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER
 * Copyright © Dreamer CMS 2019 All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.itechyou.cms.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Setter;

/**
 * @author TODAY <br>
 *         2019-10-26 22:32
 */
@Setter
public class Pagination<T> implements ListableResult<T>, Pageable {

    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 200;
    private boolean success;

    private List<T> data = Collections.emptyList();

    public Pagination() {

    }

    public Pagination(Pageable pageable) {
        this(pageable.getCurrent(), pageable.getSize());
    }

    /**
     * 分页构造函数
     *
     * @param current
     *            当前页
     * @param size
     *            每页显示条数
     */
    public Pagination(int current, int size) {
        this(current, size, 0);
    }

    public Pagination(int current, int size, int total) {
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
        this.total = total;
    }

    /**
     * empty
     * 
     * @param <T>
     */
    public static <T> Pagination<T> empty() {
        final Pagination<T> pagination = new Pagination<>();
        pagination.setRecords(Collections.emptyList());
        return pagination;
    }

    /**
     * 
     * @param <T>
     * @return
     */
    public static <T> ListableResult<T> success() {
        return success(Collections.emptyList());
    }

    /**
     * 
     * @param <T>
     * @param data
     * @return
     */
    public static <T> ListableResult<T> success(List<T> data) {
        return new Pagination<T>()//
                .setData(data)//
                .setSuccess(true)//
                .setMsg(Constant.OPERATION_OK);
    }

    @Override
    public List<T> getData() {
        return getRecords();
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    // ------------------

    /**
     * 总数
     */
    private int total = 0;
    /**
     * 每页显示条数，默认 10
     */
    private int size = Constant.DEFAULT_LIST_SIZE;
    /**
     * 当前页
     */
    private int current = 1;
    /**
     * SQL 排序 ASC 数组
     */
    private String[] ascs;
    /**
     * SQL 排序 DESC 数组
     */
    private String[] descs;

    /**
     * 是否存在上一页
     *
     * @return true / false
     */
    public boolean hasPrevious() {
        return this.current > 1;
    }

    /**
     * 是否存在下一页
     *
     * @return true / false
     */
    public boolean hasNext() {
        return this.current < this.getPages();
    }

    public int getPages() {
        if (getSize() == 0) {
            return 0;
        }
        int pages = getTotal() / getSize();
        if (getTotal() % getSize() != 0) {
            pages++;
        }
        return pages;
    }

    @JSONField(serialize = false, deserialize = false)
    public List<T> getRecords() {
        return this.data;
    }

    public Pagination<T> setRecords(List<T> records) {
        this.data = records;
        return this;
    }

    public int getTotal() {
        return this.total;
    }

    public Pagination<T> setTotal(int total) {
        this.total = total;
        return this;
    }

    public int getSize() {
        return this.size;
    }

    public Pagination<T> setSize(int size) {
        this.size = size;
        return this;
    }

    @Override
    public int getCurrent() {
        return this.current;
    }

    public Pagination<T> setCurrent(int current) {
        this.current = current;
        return this;
    }

    public String[] ascs() {
        return ascs;
    }

    public Pagination<T> setAscs(List<String> ascs) {
        if (CollectionUtils.isNotEmpty(ascs)) {
            this.ascs = ascs.toArray(new String[0]);
        }
        return this;
    }

    /**
     * 升序
     *
     * @param ascs
     *            多个升序字段
     */
    public Pagination<T> setAsc(String... ascs) {
        this.ascs = ascs;
        return this;
    }

    public String[] descs() {
        return descs;
    }

    public Pagination<T> setDescs(List<String> descs) {
        if (CollectionUtils.isNotEmpty(descs)) {
            this.descs = descs.toArray(new String[0]);
        }
        return this;
    }

    /**
     * 降序
     *
     * @param descs
     *            多个降序字段
     */
    public Pagination<T> setDesc(String... descs) {
        this.descs = descs;
        return this;
    }

    public static <T> Pagination<T> ascByCreateTime(Pageable pageable) {
        return new Pagination<T>(pageable).setAsc("create_time");
    }

    public static <T> Pagination<T> descByCreateTime(Pageable pageable) {
        return new Pagination<T>(pageable).setAsc("create_time");
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj instanceof Pagination) {
            final Pagination<?> other = (Pagination<?>) obj;

            return size == other.size
                   && current == other.current
                   && Arrays.equals(ascs, other.ascs)
                   && Arrays.equals(descs, other.descs);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, current, ascs, descs);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(code)
                .append('=')
                .append(success)
                .append('=')
                .append(msg)
                .append('=')
                .append(data)
                .append('=')
                .append(total)
                .append('=')
                .append(size)
                .append('=')
                .append(current)
                .append('=')
                .append(Arrays.toString(ascs))
                .append('=')
                .append(Arrays.toString(descs));
        return builder.toString();
    }
}
