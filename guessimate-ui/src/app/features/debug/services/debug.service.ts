import {Injectable, signal, Signal} from '@angular/core';

export type DebugLogEntry = {
  id: string;
  timestamp: Date;
  sessionId: string;
  userId: string | null;
  direction: 'SENT' | 'RECEIVED';
  messageType: 'COMMAND' | 'EVENT';
  name: string;
  payload: any;
}

@Injectable({
  providedIn: 'root'
})
export class DebugService {

  debugMode = signal<boolean>(false);

  private readonly MAX_ENTRIES = 500;
  private entries = signal<DebugLogEntry[]>([]);

  readonly debugLog: Signal<readonly DebugLogEntry[]> = this.entries.asReadonly();

  constructor() {
    const stored = localStorage.getItem("debug");
    this.debugMode.set(stored === "true");
  }

  setDebugMode(enabled: boolean) {
    this.debugMode.set(enabled);
    localStorage.setItem("debug", String(enabled));
  }

  logSent(sessionId: string, message: any) {
    const entry: DebugLogEntry = {
      id: crypto.randomUUID(),
      timestamp: new Date(),
      sessionId,
      userId: null,
      direction: 'SENT',
      messageType: 'COMMAND',
      name: message.type,
      payload: message
    };
    this.addEntry(entry);
  }

  logReceived(sessionId: string, message: any) {
    const entry: DebugLogEntry = {
      id: crypto.randomUUID(),
      timestamp: new Date(),
      sessionId,
      userId: this.extractUserId(message),
      direction: 'RECEIVED',
      messageType: 'EVENT',
      name: message.type,
      payload: message
    };
    this.addEntry(entry);
  }

  private addEntry(entry: DebugLogEntry) {
    this.entries.update(current => {
      const updated = [...current, entry];
      return updated.slice(-this.MAX_ENTRIES);
    });
  }

  private extractUserId(event: any): string | null {
    return event.userId ?? null;
  }
}
