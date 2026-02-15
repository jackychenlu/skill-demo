import asyncio
import os
import sys
from dotenv import load_dotenv

if sys.platform == 'win32':
    os.environ['PYTHONIOENCODING'] = 'utf-8'
load_dotenv()

from browser_use import Agent, Browser

async def main():
    if not os.getenv('BROWSER_USE_API_KEY'):
        print("❌ 錯誤：請在 .env 中設定 BROWSER_USE_API_KEY")
        return

    browser = Browser(headless=False)

    try:
        # 強制 AI 執行「輸入」與「搜尋」動作
        agent = Agent(
            task=(
                "1. 前往 https://www.google.com/\n"
                "2. 在頂部的搜尋框中輸入『熱門新聞』並按下 Enter 鍵\n"
                "3. 等待搜尋結果頁面載入\n"
                "4. 從搜尋結果中提取前 5 個新聞標題並回傳"
            ),
            browser=browser,
            use_vision=False,
            sensitive_data_filter=False
        )

        print("🚀 啟動本地瀏覽器，正在執行搜尋動作...")
        
        # 增加步數，因為搜尋比直接讀取多了一個步驟
        history = await agent.run(max_steps=10)
        
        print("\n🎯 --- 最終查詢結果 ---")
        print(history.final_result())

    except Exception as e:
        print(f"❌ 執行中斷: {e}")
    finally:
        print("👋 正在清理資源...")
        try:
            await browser.close()
        except:
            pass

if __name__ == "__main__":
    asyncio.run(main())