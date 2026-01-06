# Obstacle Race Game 🚀

### Android Development - Homework Assignment #1

This is a modular obstacle-avoidance racing game developed as part of the Android Development course. The project focuses on clean architecture (MVVM), state management, and UI positioning without the use of a Canvas.

---

## 🎮 Game Description
In this game, the player controls a **[Insert Character Name, e.g., Car/Spaceship]** navigating through obstacles. The objective is to survive as long as possible by switching between lanes to avoid oncoming **[Insert Obstacle Name, e.g., Rocks/Traffic]**.

### Core Features:
* **Lane-Based Movement:** Smooth transition between lanes to dodge obstacles.
* **Life System:** The player starts with a set amount of lives (e.g., 3 lives).
* **Dynamic Obstacles:** Obstacles appear at random intervals and move toward the player.
* **Real-time Scoring:** Points are awarded based on survival time or distance covered.
* **Haptic Feedback & Sound:** (Optional, if added) Vibration on collision.

---

## 🛠 Technical Implementation
The project is built entirely in **Kotlin** using **Android Studio**, following modern development standards.

### 🏗 Modular Architecture (MVVM)
To ensure the code is maintainable and modular, the project follows the **Model-View-ViewModel** pattern:
* **Model:** Contains `Data Classes` (e.g., `Player`, `Obstacle`) representing the game entities.
* **ViewModel:** The "brain" of the game. It handles the game loop, movement logic, collision detection, and score calculations. It is independent of the Android Framework’s UI classes.
* **View (UI):** Uses `LiveData` to observe state changes from the ViewModel.
* **Constraint-Based Positioning:** Per the assignment requirements, this project **does not use Canvas**. Instead, it utilizes `ConstraintLayout` and `ImageView` manipulation to manage movement and positions.

---

## 🚀 Getting Started
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/](https://github.com/)[Your-Username]/[Your-Repo-Name].git
