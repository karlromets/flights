import { Toggle } from "@/components/ui/toggle";

interface SeatToggleItemProps {
  label: string;
  icon: React.ReactNode;
  checked: boolean;
  onCheckedChange: (checked: boolean) => void;
  disabled?: boolean;
}

export function SeatToggleItem({ label, icon, checked, onCheckedChange, disabled }: SeatToggleItemProps) {
  return (
    <Toggle
      variant="outline"
      aria-label={`Toggle ${label}`}
      pressed={checked}
      onPressedChange={onCheckedChange}
      disabled={disabled}
      className="w-full justify-start gap-2"
    >
      {icon}
      {label}
    </Toggle>
  );
}
