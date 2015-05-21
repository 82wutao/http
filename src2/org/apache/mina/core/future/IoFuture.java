/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.mina.core.future;

import java.util.concurrent.TimeUnit;

import org.apache.mina.core.session.IoSession;

/**
 * 表示Session上的一个异步IO操作完成。使用IoFutureListener可以监听完成。
 * Represents the completion of an asynchronous I/O operation on an 
 * {@link IoSession}.
 * Can be listened for completion using a {@link IoFutureListener}.
 * 
 */
public interface IoFuture {
    /**
     * Returns the {@link IoSession} which is associated with this future.
     */
    IoSession getSession();

    /**
     * 这是个阻塞函数。等待异步操作的完成，附加的监听器会监听到操作完成。
     * Wait for the asynchronous operation to complete.
     * The attached listeners will be notified when the operation is 
     * completed.
     */
    IoFuture await() throws InterruptedException;

    /**
     * 这是个阻塞函数。等待异步操作的完成，附加的监听器会监听到操作完成。
     * Wait for the asynchronous operation to complete with the specified timeout.
     *
     * @return <tt>true</tt> if the operation is completed.
     */
    boolean await(long timeout, TimeUnit unit) throws InterruptedException;

    /**
     * Wait for the asynchronous operation to complete with the specified timeout.
     *
     * @return <tt>true</tt> if the operation is completed.
     */
    boolean await(long timeoutMillis) throws InterruptedException;

    /**
     * Wait for the asynchronous operation to complete uninterruptibly.
     * The attached listeners will be notified when the operation is 
     * completed.
     * 
     * @return the current IoFuture
     */
    IoFuture awaitUninterruptibly();

    /**
     * Wait for the asynchronous operation to complete with the specified timeout
     * uninterruptibly.
     *
     * @return <tt>true</tt> if the operation is completed.
     */
    boolean awaitUninterruptibly(long timeout, TimeUnit unit);

    /**
     * Wait for the asynchronous operation to complete with the specified timeout
     * uninterruptibly.
     *
     * @return <tt>true</tt> if the operation is finished.
     */
    boolean awaitUninterruptibly(long timeoutMillis);

//    /**
//     * @deprecated Replaced with {@link #awaitUninterruptibly()}.
//     */
//    @Deprecated
//    void join();

//    /**
//     * @deprecated Replaced with {@link #awaitUninterruptibly(long)}.
//     */
//    @Deprecated
//    boolean join(long timeoutMillis);

    /**
     * Returns if the asynchronous operation is completed.
     */
    boolean isDone();

    /**
     * 当future完成时，监听器会通知。
     * Adds an event <tt>listener</tt> which is notified when this future is completed.
     * If the listener is added after the completion, the listener is directly notified.
     *
     */
    IoFuture addListener(IoFutureListener<?> listener);

    /**
     * Removes an existing event <tt>listener</tt> so it won't be notified when
     * the future is completed.
     */
    IoFuture removeListener(IoFutureListener<?> listener);
}
