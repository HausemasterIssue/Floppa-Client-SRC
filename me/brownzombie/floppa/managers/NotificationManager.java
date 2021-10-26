package me.brownzombie.floppa.managers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.brownzombie.floppa.Floppa;
import me.brownzombie.floppa.util.Timer;

public class NotificationManager {

    public final List Notifications = new CopyOnWriteArrayList();

    public void AddNotification(String title, String description, Boolean forcePermanent) {
        this.Notifications.add(new NotificationManager.Notification(title, description, forcePermanent));
    }

    public static NotificationManager Get() {
        return Floppa.notifManager;
    }

    public class Notification {

        private String Title;
        private String Description;
        private Boolean ForcePermanent;
        private Timer timer = new Timer();
        private Timer DecayTimer = new Timer();
        private int DecayTime;
        private int X;
        private int Y;

        public Notification(String title, String description, Boolean forcePermanent) {
            this.Title = title;
            this.Description = description;
            this.ForcePermanent = forcePermanent;
            this.DecayTime = 2500;
            this.timer.reset();
            this.DecayTimer.reset();
        }

        public void OnRender() {
            if (this.timer.passedMs((long) (this.DecayTime - 500))) {
                --this.Y;
            }

        }

        public boolean IsDecayed() {
            return this.DecayTimer.passedMs((long) this.DecayTime);
        }

        public String GetDescription() {
            return this.Description;
        }

        public String GetTitle() {
            return this.Title;
        }

        public Boolean getForce() {
            return this.ForcePermanent;
        }

        public int GetX() {
            return this.X;
        }

        public void SetX(int x) {
            this.X = x;
        }

        public int GetY() {
            return this.Y;
        }

        public void SetY(int y) {
            this.Y = y;
        }
    }
}
