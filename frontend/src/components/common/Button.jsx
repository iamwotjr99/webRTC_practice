export default function Button({
    type = "button",
    onClick,
    children,
    disabled = false,
    className = "",
    variant = "join"
}) {
    const baseStyle = "px-4 py-2 rounded font-semibold text-white transition";

    const variantStyle = {
        join: "bg-green-500 hover:bg-green-600",
        create: "bg-blue-500 hover:bg-blue-600",
        danger: "bg-red-500 hover:bg-red-600",
        outline: "bg-indigo-100 border border-indigo-500 text-indigo-800 hover:bg-indigo-200",
    };

    const disabledStyle = "bg-gray-400 cursor-not-allowed";

    return (
        <button
            type={type}
            onClick={onClick}
            disabled={disabled}
            className={`${baseStyle}
                ${disabled 
                    ? disabledStyle
                    : variantStyle[variant]}
                ${className}`
            }            
        >
            {children}
        </button>
    )
}