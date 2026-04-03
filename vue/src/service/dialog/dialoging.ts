import {type Component, h, render, type VNode} from "vue";
import {$applicationContext} from "../../index.ts";
import {ElButton, ElDialog} from "element-plus";

export const $dialog = () => {
    const atomicAssembly = (node: VNode) => {
        const container = document.createElement('div')

        if (node.props) {
            const originalOnClose = node.props.onClose
            node.props.onClose = () => {
                originalOnClose?.()
                render(null, container)
                container.remove()
            }
            node.props.modelValue = true
        }

        if ($applicationContext) node.appContext = $applicationContext

        render(node, container)
        document.body.appendChild(container)

        return {
            close: () => {
                render(null, container)
                container.remove()
            }
        }
    }

    return {
        customize: (node: VNode) => atomicAssembly(node),
        $preset: {
            $Plain: (text: string | number, title: string) => {
                const node = h(ElDialog, {title}, {
                    default: () => h('div', {style: 'padding: 20px'}, String(text))
                })
                return atomicAssembly(node)
            },
            $confirmAndCancel: (message: string, title: string, onConfirm: () => void, onCancel?: () => void) => {
                const node = h(ElDialog, {title}, {
                    default: () => h('div', {style: 'padding: 20px'}, message),
                    footer: () => h('div', {style: 'text-align: right; padding: 10px'}, [
                        h(ElButton, {
                            onClick: () => {
                                onCancel?.()
                                node.props?.onClose()
                            }
                        }, () => 'Cancel'),
                        h(ElButton, {
                            type: 'primary',
                            onClick: () => {
                                onConfirm()
                                node.props?.onClose()
                            }
                        }, () => 'Confirm'),
                    ])
                })
                return atomicAssembly(node)
            },
            $withComponent: (component: Component, title: string, props: any = {}) => {
                const node = h(ElDialog, {title}, {
                    default: () => h(component, {
                        ...props,
                        onClose: () => node.props?.onClose()
                    })
                })
                return atomicAssembly(node)
            }
        }
    }
}