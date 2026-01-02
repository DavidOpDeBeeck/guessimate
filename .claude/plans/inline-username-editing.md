# Feature: Inline Username Editing on Estimation Page

## Overview

Allow users to edit their username directly from the estimation page by clicking an edit icon next to their name in the estimator/observer list.

## Status

**Completed** - 2026-01-02

## Scope

- Pure UI feature - backend already supports `SetUsernameCommand` and `UsernameSetEvent`
- `SessionStore.setUsername()` method already exists
- Both estimators and observers can edit their username

## Files Created

### `inline-username.component.ts`
**Path:** `guessimate-ui/src/app/features/estimation/components/inline-username.component.ts`

Reusable component for inline username editing with:
- `username` - required input for current username
- `editable` - whether to show edit button (default: false)
- `highlighted` - whether to use brand colors (default: false)
- `setUsername` - output event when username is saved

Features:
- Edit icon button triggers inline input mode
- Input with blur/Enter to save, Escape to cancel
- Auto-focus and select text on edit start
- Max 16 characters
- Saves to localStorage AND emits event for WebSocket update

## Files Modified

### `estimator-detail.component.ts`
**Path:** `guessimate-ui/src/app/features/estimation/components/estimators/estimator-detail.component.ts`

- Replaced inline username span with `<app-inline-username>` component
- Added `setUsername` output to propagate username changes
- Removed duplicate editing logic (now in shared component)

### `observer-detail.component.ts`
**Path:** `guessimate-ui/src/app/features/estimation/components/observers/observer-detail.component.ts`

- Same changes as estimator-detail
- Uses `<app-inline-username>` component

### `estimator-overview.component.ts`
**Path:** `guessimate-ui/src/app/features/estimation/components/estimators/estimator-overview.component.ts`

- Added `setUsername = output<string>()` to pass through username changes

### `observer-overview.component.ts`
**Path:** `guessimate-ui/src/app/features/estimation/components/observers/observer-overview.component.ts`

- Added `setUsername = output<string>()` to pass through username changes

### `estimation-page.component.ts`
**Path:** `guessimate-ui/src/app/features/estimation/pages/estimation-page.component.ts`

- Wired `(setUsername)="store.setUsername($event)"` to both overview components

### `username-selector.component.ts`
**Path:** `guessimate-ui/src/app/features/settings/components/username-selector.component.ts`

- Updated label/id from "displayName" to "username" for consistency

## UI Behavior

1. **Edit icon**: Small pencil icon (size-3.5) appears next to username, only for current user
2. **Click to edit**: Clicking icon switches to inline input mode
3. **Input styling**: Transparent background, bottom border only, brand colors
4. **Save triggers**: Blur or Enter key
5. **Cancel**: Escape key cancels without saving
6. **Auto-focus**: Input is focused and text selected on edit start
7. **Validation**: Max 16 characters, non-empty, different from current
8. **Persistence**: Saves to localStorage AND sends WebSocket command

## Data Flow

```
User clicks edit icon
    ↓
InlineUsernameComponent.startEditing()
    ↓
User types and blurs/presses Enter
    ↓
InlineUsernameComponent.save()
    ↓
localStorage.setItem("username", newUsername)
    ↓
setUsername.emit(newUsername)
    ↓
EstimatorDetailComponent (passes through)
    ↓
EstimatorOverviewComponent (passes through)
    ↓
EstimationPageComponent
    ↓
store.setUsername(newUsername)
    ↓
SessionStore sends SetUsernameCommand via WebSocket
    ↓
Backend broadcasts UsernameSetEvent
    ↓
All connected clients update UI
```

## Testing Notes

- No unit tests required per frontend testing guidelines
- Manual testing: Both estimator and observer roles, dark mode, various username lengths
- Verify real-time sync across multiple browser tabs/sessions
