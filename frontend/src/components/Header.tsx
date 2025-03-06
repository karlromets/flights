import { ModeToggle } from "./mode-toggle";

export function Header() {
  return (
    <div className="flex items-center justify-center w-full h-16 bg-background">
      <ModeToggle />
    </div>
  );
}
