import asyncio
import os
import sys
from dotenv import load_dotenv

# 1. 修正 Windows 終端機編碼與載入環境變數
if sys.platform == 'win32':
    os.environ['PYTHONIOENCODING'] = 'utf-8'
load_dotenv()

from browser_use import Agent, Browser

async def main():
    # 檢查 API KEY
    if not os.getenv('BROWSER_USE_API_KEY'):
        print("❌ 錯誤：請在 .env 中設定 BROWSER_USE_API_KEY")
        return

    # 2. 初始化本地瀏覽器 (視窗模式)
    browser = Browser(headless=False)

    try:
        # 3. 定義任務：明確指定 Yahoo 台灣與熱門新聞
        agent = Agent(
            task=(
                "前往 https://tw.yahoo.com/ ，"
                "在首頁的『新聞』區塊中找到『熱門』分頁並點擊，"
                "接著使用 Javascript 讀取並列出前 5 個熱門新聞標題。"
            ),
            browser=browser,
            use_vision=False,         # 關閉視覺以避開 3.14 的解析錯誤
            generate_gif=False        # 減輕負擔
        )

        print("🚀 啟動本地瀏覽器，正在查詢 Yahoo 熱門新聞...")
        
        # 執行任務 (限制步數以防迷路)
        history = await agent.run(max_steps=8)
        
        result = history.final_result()
        
        # 4. 顯示與儲存結果
        if result:
            print("\n🎯 --- Yahoo 熱門新聞前五名 ---")
            print(result)
            
            # 自動儲存到 Markdown 檔案
            with open("yahoo_hot_news.md", "w", encoding="utf-8") as f:
                f.write(f"# Yahoo 奇摩熱門新聞\n\n> 抓取時間: 2026-02-15\n\n{result}")
            print(f"\n✅ 結果已存至: {os.path.abspath('yahoo_hot_news.md')}")
        else:
            print("\n⚠️ 任務完成，但 AI 未能提取到具體標題。")

    except Exception as e:
        print(f"❌ 執行中斷: {e}")
    finally:
        print("👋 任務結束，正在關閉瀏覽器...")
        try:
            await browser.close()
        except:
            pass

if __name__ == "__main__":
    asyncio.run(main())