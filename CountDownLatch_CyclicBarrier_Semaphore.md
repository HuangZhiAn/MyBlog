# CountDownLatch

等一组线程执行完后，调用 CountDownLatch 实例的 countDown() 方法，这时，调用了 CountDownLatch 实例 await() 方法的线程开始继续执行，CountDownLatch 不能重用    
单线程 - - 多线程 - - 同步 - - 单线程

# CyclicBarrier

一组线程中，某个线程执行到某一步，执行 CyclicBarrier 实例的 await() 方法，直到所有的线程都执行了 await() 方法，所有的线程才开始继续执行，CyclicBarrier 可以重用  
多线程 - - 同步 - - 多线程

# Semaphore
设置一定数量的信号量，线程执行时调用 acquire() 方法获取信号量，获取成功后继续执行，否则等到别的线程释放信号量后才继续执行，限制了活动的线程数

<!--stackedit_data:
eyJoaXN0b3J5IjpbLTk1OTM5Mjc2OCw3ODU1Mjg4OF19
-->