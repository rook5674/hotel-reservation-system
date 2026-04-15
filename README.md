🚀 CSE241 Hotel System: GitHub Workflow Guide
Welcome to the team repository! Because our commit history is graded and our core branches are protected, everyone must follow this exact workflow.

Do not push code directly to main or dev. If you do, GitHub will block it. Follow the steps below to contribute your code safely.

🛠️ Step 0: The First-Time Setup (Do this once)
Before you write any code, you need to get the repository onto your computer.

Open your terminal or IDE terminal.

Navigate to the folder where you want to store the project.

Clone the repository:

Bash

git clone <paste-the-github-repo-link-here>
Move into the new project folder:

Bash

cd <name-of-the-folder>
🔄 Step 1: The Daily Loop (Do this EVERY time you start working)
Before you start writing Java code for your assigned task, you must ensure you have the team's latest updates and create a safe space for your work.

1. Switch to the development branch:

Bash

git checkout dev
2. Pull the latest code from the team:

Bash

git pull origin dev
3. Create your feature branch:
Name your branch based on exactly what you are working on (e.g., feature/guest-login, feature/room-class, feature/fxml-checkout).

Bash

git checkout -b feature/your-feature-name
You are now safe to open your IDE and start coding!

💾 Step 2: Saving Your Work (Committing)
As you finish logical pieces of your code (e.g., you finished the password validation method), you need to commit your changes. The professors will read these messages, so make them professional.

1. Stage your changed files:

Bash

git add .
2. Commit with a descriptive message:

Bash

git commit -m "Add password regex validation to Guest class"
(Repeat coding, adding, and committing as much as you need until your specific feature is entirely finished).

☁️ Step 3: Sending Code to GitHub (Pushing)
When your feature is completely done and tested on your computer, send it to the cloud.

Bash

git push origin feature/your-feature-name
(If this is the first time you are pushing this branch, Git might give you a slightly longer command to copy and paste. Just follow what the terminal tells you).

🤝 Step 4: The Pull Request (Merging your work)
Your code is now on GitHub, but it is not in the dev branch yet. You need to ask the team to review and merge it.

Go to the repository page on the GitHub website.

You will see a green button at the top saying "Compare & pull request". Click it.

CRITICAL: Make sure the "base" branch on the left is set to dev (NOT main).

Give your Pull Request a title and a brief description of what you did.

Click Create pull request.

Tell the team in our group chat to review it. Once someone approves it, they can click "Merge".

After it merges, delete your feature branch on GitHub to keep things clean.

🚨 The 3 Golden Rules
Never write code on main or dev: You must always type git checkout -b feature/... before typing Java.

Never branch off a branch: Always start your new tasks from dev.

Communicate: If you are getting a "Merge Conflict" error in your terminal, stop and message the group. Do not try to force it, or you might delete someone else's code!
