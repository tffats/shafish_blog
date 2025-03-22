def heap_sort(arr):
    n = len(arr)
    
    # 构建最大堆（从最后一个非叶子节点开始）
    for i in range(n // 2 - 1, -1, -1):
        heapify(arr, n, i)
    
    # 依次提取最大值并调整堆
    for i in range(n-1, 0, -1):
        arr[i], arr[0] = arr[0], arr[i]  # 交换堆顶与末尾元素
        heapify(arr, i, 0)                # 调整剩余元素的堆
    return arr

def heapify(arr, n, root):
    largest = root
    left = 2 * root + 1
    right = 2 * root + 2
    
    # 找到根、左子节点、右子节点中的最大值
    if left < n and arr[left] > arr[largest]:
        largest = left
    if right < n and arr[right] > arr[largest]:
        largest = right
    
    # 如果最大值不是根节点，则交换并递归调整
    if largest != root:
        arr[root], arr[largest] = arr[largest], arr[root]
        heapify(arr, n, largest)

# 示例
arr = [12, 11, 13, 5, 6, 7]
sorted_arr = heap_sort(arr)
print("堆排序结果:", sorted_arr)  # 输出: [5, 6, 7, 11, 12, 13]