export default function Button({
    type = "button",
    onClick,
    children,
    disabled = false,
    className = "",
    variant = "join",
    size = "md",
}) {
    const baseStyle = "rounded font-semibold text-white transition";

    const sizeStyle = {
        sm: "px-3 py-1 text-sm",
        md: "px-4 py-2 text-base",
        lg: "px-5 py-3 text-lg",
    };

    const variantStyle = {
        join: "bg-green-500 hover:bg-green-600",
        create: "bg-blue-500 hover:bg-blue-600",
        danger: "bg-red-500 hover:bg-red-600",
        outline: "bg-green-400 border border-green-600 hover:bg-green-300",
    };

    const disabledStyle = "bg-gray-400 cursor-not-allowed";

    return (
        <button
            type={type}
            onClick={onClick}
            disabled={disabled}
            className={[
                baseStyle,
                sizeStyle[size],
                disabled ? disabledStyle : variantStyle[variant],
                className
            ].join(" ")}        
        >
            {children}
        </button>
    )
}