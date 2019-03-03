# CountDownLatch

等一组线程执行完后，调用 CountDownLatch 实例的 countDown() 方法，这时，调用了 CountDownLatch 实例 await() 方法的线程开始继续执行，CountDownLatch 不能重用  
单线程 - - 多线程 - - 同步 - - 单线程
# CyclicBarrier
一组线程中，某个线程执行到某一步，执行 CyclicBarrier 实例的 await() 方法，直到所有的线程都执行了 await() 方法，所有的线程才开始继续执行，CyclicBarrier 可以重用
多线程 - - 同步 - - 多线程
<!--stackedit_data:
eyJoaXN0b3J5IjpbNzg1NTI4ODhdfQ==
-->