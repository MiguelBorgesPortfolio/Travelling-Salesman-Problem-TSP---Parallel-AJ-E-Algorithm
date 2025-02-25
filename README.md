# Travelling Salesman Problem (TSP) - Parallel AJ-E++ Algorithm  

## 📌 About the Project  
This project implements a **parallel and concurrent solution** for the **Travelling Salesman Problem (TSP)** using the **AJ-E++ evolutionary algorithm**.  
Unlike single-solution approaches, **AJ-E++ uses a population-based strategy**, improving performance through **genetic crossover and mutation operations**.  

The implementation focuses on **parallel computing** techniques using **Java multithreading**, **shared memory access**, and **synchronization mechanisms**.

---

## 🛠 Technologies Used  
- **Java (JVM - Windows/Linux Compatible)**  
- **Multithreading (Thread Pools & Parallel Processing)**  
- **Synchronized Memory Access**  
- **Semaphores & Synchronization Mechanisms**  
- **File Handling for Input Processing**  

---

## 📂 Algorithm Implementation  

### **1️⃣ AJ-E++ Evolutionary Algorithm**  
✔ Initializes a **random population of paths**  
✔ Evaluates the **total path distance** for each solution  
✔ Selects the **two best paths** from the population  
✔ Applies **PMX Crossover (Partially Mapped Crossover)** to create new paths  
✔ Mutates paths with a **probability threshold** (e.g., 1%)  
✔ Replaces the **worst paths** in the population with the new solutions  
✔ Repeats for a **fixed number of iterations or time limit**  

### **2️⃣ Parallel Implementation - Base Version**  
✔ **Creates m parallel threads** to run independent AJ-E++ instances  
✔ Each thread updates the **central memory** with its best-found solution  
✔ Uses **synchronization mechanisms** to avoid race conditions  

### **3️⃣ Parallel Implementation - Advanced Version**  
✔ Periodically **merges populations** from all threads  
✔ Selects the **best individuals** and updates all threads  
✔ Ensures global synchronization **without corrupting data**  

---
  

 
